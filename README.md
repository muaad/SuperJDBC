# SuperJDBC
Turn your Java models into Super Models

This is a lightweight Java library that lets you concentrate on your application's logic rather than database stuff. No more SQL
in your code. Yes, there is Hibernate, ActiveJDBC, ActiveJPA and so on but this is much simpler to get started with and use. 
And, it is customizable and extensible.

### Getting Started
  1. Put JAR in your classpath
  2. Have the models you want to interact with the database extend `SuperModel`
  3. This library is a bit opinionated so, make sure you follow the conventions below for this to work smoothly
  4. Create a runnable class (with a main method) to run database migrations
And, off you go!

### Conventions
  1. Model name should to be in singular form. This is mapped to a table whose name is the plural of the class name. If you provide
    a name which is already plural or whose plural form cannot be determined, 'es' will be appended to the class name. This is all
    abstracted away so you won't have a problem with it. But, it is recommended that the model names are in singular form
  
  
