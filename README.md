<!--suppress HtmlDeprecatedTag, XmlDeprecatedElement -->
<center><img alt="screenshot" src="https://cdn.modrinth.com/data/MAmi6HBa/images/28cd3601f0f474e83693522dbcc4785a409bcf18.png" /></center>

<center>
A <a href="https://modrinth.com/mod/drogtor">Drogtor</a>-style UX wrapper for Styled Nicknames.<br/>
simpler self-assignment and a strict sense of name-autonomy.<br/>
Requires  <a href="https://modrinth.com/mod/styled-nicknames">Styled Nicknames</a>.<br/>
Requires <a href="https://modrinth.com/mod/connector">Connector</a> and <a href="https://modrinth.com/mod/forgified-fabric-api">FFAPI</a> on forge.<br/>
</center>

---

Drogstyle provides a self-assignable display name system with the following commands:
 - `/nick [name]`: set your display name. If needed, also supports [P:API Formatting](https://placeholders.pb4.eu/user/text-format/) via [styled](https://modrinth.com/mod/styled-nicknames)'s config.
 - `/color [color]`: set your name color. Supports vanilla colors and #hexcodes
 - `/bio [bio]`: set your name bio (tooltip). Supports `\n` for newlines.

All commands can be called on their own to clear the set value.

### Design: Self-Ownership

None of the above commands offer any kind of permissions system, and won't.<br/>
Drogstyle force-allows self-assignment permissions in styled nicknames using a [mixin](https://github.com/sisby-folk/drogstyle/blob/1.19/src/main/java/folk/sisby/drogstyle/mixin/styled_nicknames/ConfigManagerMixin.java).<br/>
No method is provided for operators to set or clear other players' names.

#### Ranting Into The Void - Even More Design Notes:

Drogtor is designed around _"assuming you trust your players"_ - intended for things like invite-only servers.<br/>
Drogstyle expands on this by treating names as an inalienable right for self-expression, even on public servers.<br/> That means drogstyle prefers you a ban a player who won't stop abusing the mod, rather than touching their name.

### Utility Commands

Player usernames can also be checked via hovering over name formatting (e.g. `#`), or clicking.

- `/drogstyle username [name]` - looks up a player's username (for mods that show display names outside of chat.)
- `/drogstyle reload` - reloads the styled nicknames configuration.

---

### Afterword

All mods are built on the work of many others.

This mod specifically exists out of respect of the simplistic and self-expression focused design of Drogtor: The Nickinator, and seeks to continue it with the power of Styled Nicknames as a backend - please support the original mods, and don't send them drogstyle-related issues. 

This mod is included in [Tinkerer's Quilt](https://modrinth.com/modpack/tinkerers-quilt) - our modpack about rediscovering vanilla.

We're open to suggestions for how to implement stuff better - if you see something wonky and have an idea - let us know.
