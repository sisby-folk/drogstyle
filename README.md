<!--suppress HtmlDeprecatedTag, XmlDeprecatedElement -->
<center><img alt="screenshot" src="https://cdn.modrinth.com/data/MAmi6HBa/images/28cd3601f0f474e83693522dbcc4785a409bcf18.png" /></center>

<center>
Simple, self-assignable display names.<br/>
Requires  <a href="https://modrinth.com/mod/styled-nicknames">Styled Nicknames</a>.<br/>
Requires <a href="https://modrinth.com/mod/connector">Connector</a> and <a href="https://modrinth.com/mod/forgified-fabric-api">FFAPI</a> on (neo)forge.<br/>
</center>

---

**Drogstyle** adds the following commands:

- `/nick [name]`: set your display name, with [Placeholder Simplified Text Format](https://placeholders.pb4.eu/user/text-format/) available
- `/color [color]`: set your name color, vanilla colors or #RRGGBB
- `/bio [bio]`: set your name bio (tooltip), with `\n` for newlines

All commands can be called on their own to clear the set value.

### Limitations

None of the above commands offer any kind of permissions system, and won't.<br/>
Drogstyle force-allows self-assignment permissions in styled nicknames using a [mixin](https://github.com/sisby-folk/drogstyle/blob/1.19/src/main/java/folk/sisby/drogstyle/mixin/styled_nicknames/ConfigManagerMixin.java).<br/>
Drogstyle removes all original styled nicknames commands to avoid clashes.<br/>

### Utility Commands

Player usernames can be checked via hovering over name formatting (e.g. `#`), or clicking.

- `/drogstyle username [name]`: look up a player's username via their display name
- `/execute as [username] run nick`: clear another player's username
- `/drogstyle reload`: reload the styled nicknames configuration

---

### Afterword

All mods are built on the work of many others.

This mod specifically exists out of respect for the design of [Drogtor The Nickinator](https://modrinth.com/mod/drogtor), and seeks to continue it with the power and flexibility of Styled Nicknames - over which drogstyle is only a thin wrapper! - please support the original mods, and don't send them drogstyle-related issues.

This mod is included in [Tinkerer's Quilt](https://modrinth.com/modpack/tinkerers-quilt) - our modpack about rediscovering vanilla.

We're open to better ways to implement our mods. If you see something odd and have an idea, let us know! 

---

<center>
<b>Tinkerer's:</b> <a href="https://modrinth.com/modpack/tinkerers-quilt">Quilt</a> - <a href="https://modrinth.com/mod/tinkerers-smithing">Smithing</a> - <a href="https://modrinth.com/mod/origins-minus">Origins</a> - <a href="https://modrinth.com/mod/tinkerers-statures">Statures</a> - <a href="https://modrinth.com/mod/picohud">HUD</a><br/>
<b>Loveletters:</b> <a href="https://modrinth.com/mod/inventory-tabs">Tabs</a> - <a href="https://modrinth.com/mod/antique-atlas-4">Atlas</a> - <a href="https://modrinth.com/mod/portable-crafting">Portable Crafting</a> - <i>Drogstyle</i><br/>
<b>Others:</b> <a href="https://modrinth.com/mod/switchy">Switchy</a> - <a href="https://modrinth.com/mod/crunchy-crunchy-advancements">Crunchy</a> - <a href="https://modrinth.com/mod/starcaller">Starcaller</a><br/>
</center>
