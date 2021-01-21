# actionmc-mod
Companion Minecraft mod to the ActionMC server

Adds simple features such as a service status check and built-in skin server compatibility.

Tested and confirmed to work on its own and with OptiFine 1.2.5 HD U C7 (requires deletion of nl.class).
Compatibility with Nodus will be added eventually.

## Compiling
MCP 6.2 is required to compile this. It should be placed in a directory named `mcp62` or nothing will work.

To compile, simply run `compile.sh` with Bash on a typical GNU/Linux distribution. WSL is untested.

## Downloading/Using
If you don't want to bother compiling this, grab the latest release [here](https://github.com/notvelleda/actionmc-mod/releases/latest). It's a standard JAR mod, so you have to extract it over the Minecraft JAR file (make sure to delete `META-INF` or just click "Add to Minecraft.jar" in MultiMC). This mod causes no conflicts with OptiFine 1.2.5 HD U C7, however in order for it to work properly you *must* preserve the original Minecraft `nl.class` (FontRenderer).

yes i know this README is awful it's 2:40 am and i'm just trying to get this done leave me alone
