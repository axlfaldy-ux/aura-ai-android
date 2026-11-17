# AURA AI Android v0.2

Android dashboard prototype for the AURA AI affiliate production agent.

## Included in this version
- Dark Android dashboard
- Reference Image Studio screen
- User's primary shoe reference photo bundled in the app
- Change primary reference photo using Android image picker
- Select a product photo using Android image picker
- Prompt template text field (prompt is not finalized yet)
- Explicit disabled generation button until an image-generation integration is implemented
- GitHub Actions workflow to build a debug APK

## Not connected yet
This is still a UI prototype. It does **not** connect to TikTok Shop, ChatGPT Image / OpenAI image generation, Google Flow, WhatsApp, or FFmpeg. Selecting images only previews them locally in the app; it does not upload them to a server or generate a new image.

## Build on GitHub
1. Create a private repository.
2. Upload the *contents* of this folder to the repository root (not the enclosing folder).
3. Open the Actions tab and allow workflows if GitHub asks.
4. A push to `main` starts the workflow automatically.
5. After it succeeds, open the workflow run and download `aura-ai-debug-apk` from Artifacts.

## Build environment
- JDK 17
- Android SDK Platform 35
- Gradle 8.9
