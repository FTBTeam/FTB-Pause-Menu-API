# FTB Pause menu API

Custom pause menu for unification API for adding new buttons and elements to the pause menu. To achieve this, we fully replace the pause menu with our own implementation to ensure no mod is able to bypass our API. We only recommend using this mod in your modpack when you know what should be on your pause menu

## Features

- Developer friendly API
- Clean default pause menu
- Customisable pause menu
- Built in support for
  - Discord & Github links (Top left corner)
  - Tips (By Darkhax)
  - Just Zoom (By Keksuccino)

## Developer Notes

- Although this API introduces a unified injection method to the Pause Menu, it's recommended that you do not over populate the pause menu with buttons. This is partially modpack specific, so if you do add buttons, we highly recommend you allow them to be disabled via your mods config.
- Overlapping is possible, and we've intentionally not prevented it. 
- We've baked in padding for items, ordering and locations to ensure that the pause menu is clean and easy to extend

## Integration

Integrating your Pause Menu button is simple! Firstly, if you're using a mixin already, keep using it! As we completely replace the pause menu, your mixin will still work but will not show.

### Adding the API to your project

```groovy
repositories {
  maven {
    name = "FTB Pause Menu API"
    url = "https://maven.ftb.dev/releases"
    content = {
      includeGroup "dev.ftb.mods"
    }
  }
}

dependencies {
  // Use the correct implementation call for your toolchain
  implementation "dev.ftb.mods:ftb-pause-menu-api:${project.pause_menu_api_version}"
  
  // If you only want to depend on the API, you can use the following
  api "dev.ftb.mods:ftb-pause-menu-api:${project.pause_menu_api_version}:api"
}
```

### Registering your provider

```java
public class MyPauseItem implements PauseItemProvider {
  ...
}
```

**Somewhere in your mods startup**
```java
PauseMenuApi.get().registerPauseItem(MenuLocation.TOP_RIGHT, new MyPauseItem());
```

### Creating your provider

We've tried to keep the 'contract' here as close to vanilla as possible. You can still use vanilla buttons / widgets and you can manually render your own items by hand.

You will have to provide a couple of things. Specifically:

- The size of your widget by overriding `width` and `height` (If it's just a normal button, it's your buttons width by the button height (normally 20))
- If you want to do a custom render, you will need to override `render` and `hasRender` to return `true`
- If you want to add normal buttons / widgets, you can override `init` and add your buttons there

### The init

Init is very close to the way vanilla works but you are provided an `x`, and `y` which is relative to your location you registered against. Your 'provider' will be aligned correctly based on the `width` and `height` provided so you do not need to adjust your rendering based on the location selected.

```java
@Override
public @Nullable ScreenWidgetCollection init(MenuLocation target, ScreenHolder screen, int x, int y) {
  // You need to return a collection of widgets  
  ScreenWidgetCollection screenWidgetCollection = ScreenWidgetCollection.create();

  // Add your widget just like you would in a vanilla screen
  screenWidgetCollection.addRenderableWidget(Button.builder(Component.literal("Original"), btn -> {
    PauseMenuScreen.DISABLE_CUSTOM_PAUSE = true;
    Minecraft.getInstance().setScreen(new PauseScreen(true));
  }).width(70).pos(x, y).build());

  // Provide back the collection
  return screenWidgetCollection;
}
```

### Examples

You can see some examples of providers by looking in our `dev.ftb.mods.pmapi.menu.providers` package.

## Support

- For **Modpack** issues, please go here: https://go.ftb.team/support-modpack
- For **Mod** issues, please go here: https://go.ftb.team/support-mod-issues
- Just got a question? Check out our Discord: https://go.ftb.team/discord

## Licence

All Rights Reserved to Feed The Beast Ltd. Source code is `visible source`, please see our [LICENSE.md](/LICENSE.md) for more information. Any Pull Requests made to this mod must have the CLA (Contributor Licence Agreement) signed and agreed to before the request will be considered.

## Keep up to date

[![](https://cdn.feed-the-beast.com/assets/socials/icons/social-discord.webp)](https://go.ftb.team/discord) [![](https://cdn.feed-the-beast.com/assets/socials/icons/social-github.webp)](https://go.ftb.team/github) [![](https://cdn.feed-the-beast.com/assets/socials/icons/social-twitter-x.webp)](https://go.ftb.team/twitter) [![](https://cdn.feed-the-beast.com/assets/socials/icons/social-youtube.webp)](https://go.ftb.team/youtube) [![](https://cdn.feed-the-beast.com/assets/socials/icons/social-twitch.webp)](https://go.ftb.team/twitch) [![](https://cdn.feed-the-beast.com/assets/socials/icons/social-instagram.webp)](https://go.ftb.team/instagram) [![](https://cdn.feed-the-beast.com/assets/socials/icons/social-facebook.webp)](https://go.ftb.team/facebook) [![](https://cdn.feed-the-beast.com/assets/socials/icons/social-tiktok.webp)](https://go.ftb.team/tiktok)
