// Create an HTML element
Div layout = new Div();

// Use TextField for standard text input
TextField textField = new TextField("Your name");

// Button click listeners can be defined as lambda expressions
Button button = new Button("Say hello",
          e -> Notification.show("Hello!"));

// Add the web components to the HTML element
layout.add(textField, button);
