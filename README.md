# Typescript to Kotlin 

Command line and Intellij IDEA plugin to convert a Typescript file or project to Kotlin/Js

## How it works

It uses the tool [karakum](https://github.com/karakum-team/karakum) to convert a Typescript file or project to Kotlin/Js

Makes a scan of all typescript files, creates a list of the files and calls the tool to convert the code

The generated kotlin code may not be "as you would use it" but is a good approximation that you can then copy/paste and
adapt to what you need

There is no distribution, so you'll need to build either of the modules (command-line or plugin)

Only works on mac. Can be modified to work in Windows or linux as well (because of the commands used and compose dependencies)

## Command line

It's a kotlin jvm command line application. Just build as any other kotlin project

You can call

```
ttk generate -i path/to/my/file/or/folder
```

Arguments:
```
-i -> Input file or directory (required)
-o -> Output file or directory (optional)
-w -> Working directory (optional)
```

## Plugin

Uses [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform) for the UI

You can run the "Run Plugin" configuration to run the plugin in a sandbox or build as any other Intellij Plugin

To install it in your normal workspace, you need to create the distribution as any other Intellij Plugin

### Light Theme
![light theme](content/light%20theme.png)

### Dark Theme
![dark theme](content/dark%20theme.png)