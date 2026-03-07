# 2009Scape Web Client Notes

## Goal

Build a browser-facing web client for the Java server and client in this repo.

Top priorities:

1. Support browser networking through websocket transport on the server.
2. Bring up a browser-hosted Standard Detail client path first.

## Current state

### Server

- Optional websocket listener exists on a separate port.
- It reuses the existing binary handshake/login/game packet pipeline.
- Config lives in `Server/worldprops/default.conf`:
  - `websocket_enabled`
  - `websocket_port`
- Default websocket port is `53594 + world_id` when `websocket_port = 0`.

### Client

- A transport seam exists via `rt4.GameSocket`.
- Desktop TCP still uses `BufferedSocket`.
- `FrameBuffer.createHeadless(...)` exists for browser/headless raster targets.

### Web client

- Dedicated TeaVM module exists at `rt4-client/web-client`.
- `./gradlew :web-client:generateJavaScript` succeeds.
- `./gradlew :web-client:generateWasmGC` is currently blocked by a TeaVM wasmGC internal `NullPointerException` after the browser boot/state path was widened.
- Browser bootstrap currently renders with `SoftwareRaster` and `Rasteriser`.
- Browser bootstrap now talks to the server websocket for JS5/cache traffic.
- Browser bootstrap now loads all `28` archive indices.
- Local server cache currently exposes `29` JS5 indices via `main_file_cache.idx255`.
  - The browser bootstrap must size checksum/archive arrays from the live master index, not from the old hardcoded `28`.
- Browser bootstrap loads boot archives `3`, `8`, and `13` after index bootstrap.
- Browser bootstrap can open a second websocket for the game/login protocol and perform prelogin or login handshakes.
- Browser bootstrap now renders the real title background and logo from cache archive `8` through software-only sprite decoding.
- Browser bootstrap keeps title-screen text/progress on browser-owned software primitives for now instead of the shared `Font.renderCenter(...)` path.
- Browser bootstrap now draws a browser-managed login panel over the real title assets and feeds typed credentials into the websocket login client.
- Browser bootstrap now also has a browser-local renderer for the real cached login interface group from archive `3`.
  - The browser path must not touch `GameShell` or the monolithic `client` class; TeaVM wasmGC breaks as soon as either class becomes reachable.
  - The current authentic-login renderer stays on `InterfaceList`/`LoginManager`/`Cs1ScriptRunner`, but resolves sprites through software-only loaders and skips the flame widget.
- Browser websocket login is restartable from the browser UI instead of being locked to query-string credentials.
- Browser bootstrap now maps DOM mouse, wheel, and keyboard events into the existing `rt4.Mouse` and `rt4.Keyboard` queues.
- `rt4.Mouse` and `rt4.Keyboard` are now browser-safe state holders; desktop AWT listeners live in `DesktopMouseInput` and `DesktopKeyboardInput`.
- The generated page now prefers the TeaVM WebAssembly GC target from `build/web/index.html`.
- Runtime packaging is driven through TeaVM's `buildWasmGC` task and includes `build/web/wasm-gc/web-client.wasm-runtime.js`.
- `?target=js` forces the JavaScript fallback when debugging browser issues.
- Shared runtime stubs now intentionally bias the codebase toward browser mode:
  - `Fonts.load(...)` is forced down the software-font path.
  - `Fonts.drawTextOnScreen(...)`, `Sprites.load(...)`, `TitleScreen.load(...)`, and `InterfaceList.method1596(...)` are now flattened to software/browser-safe behavior.
  - `DisplayMode`, `Cheat`, `PluginRepository`, `TracingException`, browser control helpers, and world-list fetch now have browser-safe no-op or reduced behavior.
  - `client.method930()` is stubbed so desktop audio reinit does not get pulled into browser reachability.
  - This was done to keep the wasmGC build alive while probing `ScriptRunner`/login hook reachability.
- Browser boot now has a shared-state bridge instead of only browser-local cache/login renderers:
  - `client.browserInstallBootstrap(...)` seeds shared client state from the browser JS5 bootstrap.
  - `client.browserMainLoad()` mirrors the safe subset of the desktop `mainLoad()` stages through login-screen activation.
  - `client.browserMainUpdate()` runs a software-only browser tick for interface/script progression without the desktop GL/login-camera path.
  - `BrowserClientRuntime` advances those shared states on fixed ticks and reports load/game-state transitions into the browser console.
  - Browser bootstrap now seeds a real one-world `WorldList` entry instead of only flipping `WorldList.loaded = true`.
    - Reason: the authentic login UI uses shared world-list opcodes for world labels and world selection.
    - Current browser contract: the selected websocket endpoint and the seeded shared world metadata must describe the same world.
  - Future multi-world note: if browser mode grows beyond one world, the websocket target must be derived from the selected shared `WorldList` entry instead of staying fixed.
- Browser in-game protocol now reaches `gameState 30` and the browser host now attempts a real software viewport instead of always showing the placeholder.
  - Current browser-safe render seam is intentionally incomplete:
    - `ScriptRunner.browserRenderViewportSoftware(...)`
    - `SceneGraph.method2954Software(...)`
    - `SceneGraph.method3292Software(...)`
    - `SceneGraph.method4245Software(...)`
  - Current browser exclusion: the first visible world pass is terrain-only.
    - Actor/entity draws are intentionally skipped in `method4245Software(...)`.
    - Reason: shared entity renderers still reach desktop GL code through paths like `Player.render(...)`.
  - Current browser exclusion: in-game overhead rendering is disabled.
    - `drawOverheadsSoftware(...)` exists, but the browser viewport does not call it yet.
    - Reason: shared font rendering still reaches `GlRaster`, and overhead sprite loads still reach `GlAlphaSprite`.
  - Current browser exclusion: the post-login browser world pass does not yet draw:
    - players
    - NPCs
    - scenery entities
    - object stacks
    - overhead chat/hit splats/headicons
  - Current fallback behavior: if the browser-safe in-game raster throws, `WebClientMain` logs the failure once and falls back to the connected placeholder instead of hard-crashing the page.
- `GameShell` is no longer allowed to inherit from `Applet` or any other AWT host class.
  - Reason: TeaVM's JavaScript backend was emitting a bundle that crashed immediately on missing `java.applet.Applet` / `java.awt.Panel` / AWT listener symbols.
  - Current browser-first shape: `GameShell` is now a plain runtime host object with stubbed host-navigation/parameter helpers.
  - `SignLink.applet` is now an untyped host handle instead of an `Applet`.
  - `GameCanvas` calls back into `GameShell` directly instead of depending on `Component` inheritance.
  - Future WebGL/host note: keep new browser work off AWT inheritance and AWT listener interfaces; use explicit host adapters instead.

## Why the browser path is staged

The original client is not browser-safe as-is:

- `GameShell` still contains a lot of desktop window/canvas logic, but it no longer inherits from `Applet`.
- `GlRenderer` depends on JOGL/LWJGL and desktop GL context management.
- `SignLink` owns desktop filesystem, socket, cursor, and fullscreen services.
- Shared in-game rendering is still mixed, not cleanly software-isolated.
  - `SceneGraph` traversal now has browser-safe software forks, but shared entity rendering is still GL-reachable.
  - Known remaining exclusions to remove:
    1. `Player.render(...)`
    2. `Npc.render(...)`
    3. shared overhead/font rendering through `Font.render(...)`
    4. GL-backed sprite creation paths such as `GlAlphaSprite`

The current approach is to lift usable subsystems first:

1. software raster output
2. websocket transport
3. JS5/cache loading
4. browser shell/input
5. real game boot path

## Files to know

- Server websocket entry:
  - `Server/src/main/core/net/websocket/GameWebSocketServer.java`
  - `Server/src/main/core/net/websocket/WebSocketIoSession.java`
- Browser transport:
  - `rt4-client/web-client/src/main/java/rt4/web/WebSocketGameSocket.java`
  - `rt4-client/web-client/src/main/java/rt4/web/WebSocketConfig.java`
- Browser JS5 bootstrap:
  - `rt4-client/web-client/src/main/java/rt4/web/BrowserJs5Client.java`
- Browser game/login bootstrap:
  - `rt4-client/web-client/src/main/java/rt4/web/BrowserLoginClient.java`
- Shared cache seam:
  - `rt4-client/client/src/main/java/rt4/Js5CacheQueue.java`
- Browser rendering/bootstrap:
  - `rt4-client/web-client/src/main/java/rt4/web/WebClientMain.java`
  - `rt4-client/web-client/src/main/java/rt4/web/StandardDetailRenderer.java`
  - `rt4-client/web-client/src/main/java/rt4/web/BrowserFramePresenter.java`
  - `rt4-client/web-client/src/main/java/rt4/web/BrowserInputBridge.java`
  - `rt4-client/web-client/src/main/java/rt4/web/BrowserTitleScreen.java`
- Shared browser input shims:
  - `rt4-client/client/src/main/java/rt4/Mouse.java`
  - `rt4-client/client/src/main/java/rt4/Keyboard.java`
  - `rt4-client/client/src/main/java/rt4/DesktopMouseInput.java`
  - `rt4-client/client/src/main/java/rt4/DesktopKeyboardInput.java`
- Browser entry page:
  - `rt4-client/web-client/src/main/webapp/index.html`

## Running the current bootstrap

1. Enable websocket transport in `Server/worldprops/default.conf`.
2. Start the server.
3. Build web output:
   - `cd rt4-client`
   - `./gradlew :web-client:assemble`
4. Serve the repo root or `rt4-client/web-client/build/web`.
5. Open:
   - `/rt4-client/web-client/build/web/index.html`
6. Optional websocket override:
   - `?ws=ws://127.0.0.1:53595/`
7. Optional target override:
   - `?target=js`
8. Optional login bootstrap:
   - `?username=your_name&password=your_pass`

## Immediate next steps

1. Keep pushing the authentic cached login interface until the browser-managed panel can be removed.
2. Reintroduce post-login/shared login loops incrementally behind browser-safe wrappers instead of calling `LoginManager.loop()`/`CreateManager.loop()` directly.
3. Isolate the current TeaVM wasmGC crash; the JavaScript target is the working validation target until that compiler bug is understood.
4. Extract browser-safe host state for dimensions/timing/input instead of pulling `GameShell` into the web graph.
5. Keep audio stubbed in browser mode until there is a real browser audio backend; do not route the current desktop `AudioSource` stack into TeaVM.
6. Audit other desktop-only exception/reporting paths that may still leak `Applet`/AWT reachability into browser-only code.
7. Decide whether world-list traffic should also move to websocket or be skipped entirely in browser mode.
8. Next major refactor: keep stripping `GameShell` and `SignLink` of desktop-only AWT ownership instead of reintroducing it through new shared hooks.
  - Attempting to force `InterfaceList.method1626(...)` through the current shell reached `ScriptRunner`, which immediately proved the remaining blockers are the shell/runtime abstractions themselves.
  - The last stable browser build still uses the browser-local login renderer; direct shared login-script activation was intentionally backed out after validation.

## Guardrails

- Prefer reusing existing `rt4` engine/client code over rewriting systems in JavaScript.
- Keep browser-specific code inside `rt4-client/web-client` unless a reusable seam is required.
- Keep desktop client behavior unchanged unless a shared abstraction is necessary.
- Use TeaVM's packaging tasks instead of hand-assembling wasm runtime files.
- When browser code replaces an auto-detect GL path with a software-only path, leave a note explaining that it is a temporary WebGL compatibility shim.
- When browser code introduces a terrain-only or scene-only render path, document exactly which draw categories are being skipped and where they must be reintroduced.
  - Current tracked exclusions:
    - `SceneGraph.method4245Software(...)` skips entity draws for the first browser world pass.
    - `ScriptRunner.browserRenderViewportSoftware(...)` does not call the shared in-game overhead path.
- Do not fork or globally stub the shared GL stack unless there is no smaller seam available.
  - Prefer browser-local software shims at the call site first.
  - Reason: broad GL stubbing in shared `rt4` code risks breaking the desktop client and makes a future WebGL backend harder to reintroduce cleanly.
- When browser code bypasses AWT listeners and writes directly into `rt4.Mouse` or `rt4.Keyboard`, document it as a browser-host shim so a future WebGL/browser shell can replace it cleanly.
- When TeaVM wasmGC rejects richer browser bindings, prefer pushing DOM work into `@JSBody` or tiny browser-host adapters rather than re-coupling browser code to desktop listener classes.
- When browser work needs shared login/render code, first check whether the class reaches `GameShell` or `client`.
  - If it does, extract the small piece of host state needed for browser mode instead of making those desktop shell classes reachable from TeaVM.
- Browser-first exception for this branch:
  - The user explicitly approved breaking desktop compatibility if needed to unblock the wasm client.
  - If the next `GameShell`/`SignLink` refactor requires invasive shared changes, prefer that over piling on more browser-only hook stubs.
