# Limbo

Limbo is Warzone's open sourced plugin for syncing ranks and levels in the lobby server built using Kotlin.

**Note:** This plugin only work alongside the Warzone [Mars](https://github.com/Warzone/mars) plugin.

## Configuration

By default, this plugin points to the Warzone API, but if you are using it for your own use, you can create a config to override these defaults.

Config Values:
- ``api.url`` - Sets the url for the Mars API

## Commands

Currently, the plugin only has 1 command, ``/refresh``. This command is used to refresh the plugin's cache.

Permission Node: ``limbo.refresh``
