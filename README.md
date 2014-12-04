# sbt meow

An sbt plugin for viewing cat pictures. Because if sbt is going to download the entire Internet, it damn well ought to include some cat pictures.

## Install

Add the following to your sbt `project/plugins.sbt` file:

```scala
resolvers += Resolver.url(
  "bintray-thricejamie-sbt-plugins",
    url("http://dl.bintray.com/thricejamie/sbt-plugins"))(
        Resolver.ivyStylePatterns)

addSbtPlugin("com.37pieces" % "sbt-meow" % "0.1")
```

_NOTE_ this plugin targets sbt 0.13.

## Usage

Your project now supports the `meow` task which will download a random cat picture from the web and render it to your terminal in brilliant ASCII.

    > meow

Bam. Cat picture.

## Caveats

Currently only tested with any thoroughness using bash on OS X. Results on Windows will likely be less than impressive.

## Acknowledgements

* [The Cat API](http://thecatapi.com/) for providing a ready source of cat images.
* The [scala-ascii-art](https://github.com/cb372/scala-ascii-art) repo for some inspiration and algorithm ideas.
* [This really nice gist](https://gist.github.com/MicahElliott/719710) for a great color translation table that I adapted to my needs.

## License

Copyright (c) 2014 Jamie Paulson

Published under The MIT License, see LICENSE
