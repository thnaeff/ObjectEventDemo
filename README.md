# ObjectEventDemo

**Graphical display of the component/object tree of an application and visual analyzation of all component events**

This library can be used to visually analyze the object tree of an application and all component events of a user interface. 
It started out as a simple project to visualize component boundaries, and ended up as a great tool to see the application 
structure and watch events happening in real time. Changes in the visible components are reflected in the object tree 
automatically and no changes to components on an existing application are needed.

![Download and start this demo Java application to try it out](ObjectEventDemo.jar)


## Example

A simple application window as example:
![An example user interface](ObjectEventDemo_example.png?raw=true "An example user interface")

If this example window should be analyzed, the following code will do it (f is the JFrame):
```java
ObjectEventDemo debugger = new ObjectEventDemo(f);
debugger.setLayeredPane(f.getLayeredPane());
debugger.update();
```

This creates a layered pane over your user interface and interacts with your mouse pointer, showing the boundaries of components, 
the component type, the value name (if one exists) and to which parent container the current container is added to.

![A user interface with component boundaries and info](ObjectEventDemo.png?raw=true "A user interface with component boundaries and info")


To go further, the object tree and all component events can be alalyzed in an additional frame:
```java
JFrame fInfo = debugger.createEventInfoFrame();
fInfo.setVisible(true);
```

This opens up a info frame whith all the component and event information. 
![Object tree and event info frame](ObjectEventDemo_infopanel.png?raw=true "Object tree and event info frame")


The example user interface and the info frame are mutually updated when the current hovered component changes. Selecting a component 
in the object tree marks the selected component on the user interface, and hovering over a component in the user interface 
marks the hovered component in the object tree. Also, each event on any of the selected objects in the tree is indicated 
by a green bullet in the event list, and the values of the event object methods are updated in realtime.

This example code shows that no changes are needed on the example user interface. The frame of the example user interface is 
simply given as parameter in the `ObjectEventDemo` constructor. Any component can be given in the constructor to analyze 
parts of an application.


