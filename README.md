# Energy

Energy is a lightweight java eventbus.

### Adding Energy into your project:
```gradle
repositories {
    maven { url = 'https://jitpack.io' }
}
dependencies { 
    implementation 'org.quantumclient:Energy:VERISON'
}
```

## Usage
### Events
Making a new Event
```Java
public TestEvent extends Event {
    String string;
    
    public TestEvent(String string) {
	    this.string = string;
    }
    
    public String getString() {
	    return this.string;
    }   
    
}
```

### Posting
To post an event

```Java
    public void coolThings() {
         EventBus.post(new TestEvent("test"));
        // your code here
    }   
```
### Registering
registering and unregistering a listener
```Java
    public void onEnable() {
        Eventbus.register(this);
    }
    
    public void onEnable() {
        Eventbus.unregister(this);
    }
```
### Subscribing
Subscribing to an event
```Java
    @Subscribe
    public void onTestEvent(TestEvent event) {
        System.out.print(event.getString);
        //your other code here
    }

```
For any other help and to get notified about updates join our [discord](https://discord.gg/h8EQyuYTK7)
.
