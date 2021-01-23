# PortalNK

**A Nukkit plugin implementation of the Portal TCP socket API.**

## Usage:

1) Make sure you already have Portal set up
2) Edit config.yml and update the credentials/information to match your Portal configuration
3) Run the server and wait for the connection to authenticate. 
   - If successful, you should see `Authentication with Portal was successful!` in console 
   - If the connection failed to authenticate, you will see an error telling you what is wrong 
4) The server is now connected to the proxy and can communicate using the API


## API:

### Sending packets:

- Example of sending transfer request packet:
```java
public void transferPlayer(Player player, String group, String server) {
    TransferRequestPacket pk = TransferRequestPacket.create(player.getUniqueId(), group, server);
    Portal.getInstance().getThread().addPacketToQueue(pk);
}
```

### Handling packets:

- Example of handling player info response packet:

```java
@EventHandler
public void onPortalPacketReceive(PortalPacketReceiveEvent event){
    Packet packet = event.getPacket();
    if(packet instanceof PlayerInfoResponsePacket) {
        if(!Server.getInstance().getPlayer(packet.uuid).isPresent()) { // checks if player is still on the server
            return;
        } 
        
        this.getLogger().info(Server.getInstance().getPlayer(packet.uuid).get().getName() + "'s ip is " + packet.address);
    }
}
```


## Configuration:

```yaml
proxy-address: "127.0.0.1"
socket:
  port: 19131
  secret: ""
server:
  group: "Hub"
  name: "Hub-1"
```

