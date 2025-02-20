### How to use this plugin?
Add this plugin to your project build script, take `build.gradle` as example.
```
plugins {
    id 'com.bamboo.fmpp' version '0.1'
}

tasks.register('fmppMain', com.bamboo.fmpp.FmppTask) {
    inputs.dir("path-to-input")
    config.set(file("path-to-config-file"))
    defaultConfig.set(file("path-to-default-config-file-if-any"))
    templates.set(file("path-to-template-file"))
    output.dir("path-to-output")
}
```
Internally, this plugin use fmpp to generate source file with freemarker template, see [fmpp](https://fmpp.sourceforge.net/) for more details.
