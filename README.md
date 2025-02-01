**Description:**  
vLobbyConnect is a Velocity plugin that automatically directs players to the correct lobby based on their Minecraft version upon joining the server. This ensures seamless matchmaking and prevents version incompatibility issues.  

### **Features:**  
✅ **Automatic Version Detection** – Identifies the player’s Minecraft version on join.  
✅ **Smart Lobby Assignment** – Sends players to an available lobby that matches their version.  
✅ **Multiple Lobby Support** – Supports both 1.20+ and 1.8 versions with predefined lobbies.  
✅ **Fallback Handling** – If a lobby is full, the player is sent to another available option.  
✅ **Efficient & Lightweight** – Optimized for performance with minimal impact on server resources.  
✅ **Logging & Debugging** – Provides logs for join events and redirections.  

### **Lobby Mapping:**  
- **1.20+ Players →** `lobby1` or `lobby2`  
- **1.8 Players →** `lobby3` or `lobby4`  

### **Configuration:**
To configure the lobby names, edit the `config.yml` file:

```yaml
lobbies:
  1.20lobby1: "lobby1"
  1.20lobby2: "lobby2"
  1.8lobby1: "lobby3"
  1.8lobby2: "lobby4"
```

These names must match the server entries defined in your velocity.toml.
