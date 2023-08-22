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

To check a player's username for commands, simply click their name in chat - which will autofill `/tell [username]`.

### Design: Self-Ownership

Drogstyle doesn't support any kind of permissions system, and won't.
It deliberately breaks the basic permissions in styled nicknames using [a mixin](https://github.com/sisby-folk/drogstyle/blob/1.19/src/main/java/folk/sisby/drogstyle/mixin/styled_nicknames/ConfigManagerMixin.java).
Drogtor also doesn't provide a method for server operators to set or clear other players' nicknames, and won't.

#### Ranting Into The Void - Even More Design Notes:

Drogtor is designed around _"assuming you trust your players"_ - intended for things like invite-only servers.<br/>
Drogstyle is designed around expanding this idea to public servers - by treating name-setting as an inalienable right for self-expression.<br/> That means if someone won't stop abusing the nickname system on your server, drogstyle prefers you ban them, rather than being allowed to touch their name.


---

### Afterword

All mods are built on the work of many others.

This mod is very specifically a drogtor-flavoured facade with functionality _entirely_ provided by Styled Nicknames - please support the full mod! (and don't pester them with drogstyle issues).

This mod is included in [Tinkerer's Quilt](https://modrinth.com/modpack/tinkerers-quilt) - our modpack about rediscovering vanilla.

We're open to suggestions for how to implement stuff better - if you see something wonky and have an idea - let us know.
