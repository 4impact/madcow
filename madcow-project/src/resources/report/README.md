# Treble

"I'm all about that bass, about that bass, no treble"

Well actually, Treble is a _base_ project for web projects.

Building a simple html website, jquery plugin or library? You're halfway there by using Treble - an opinionated module grunt-based layout.

_Yes, it's not Yeoman_

Who's this for? People that want a bit more control & don't want to listen to the man! Oh wait, I guess I'm telling you how to structure your projects.
Well errr, for people who like this layout I guess. Like me. Mostly me. Probably just for myself!
If you do find it useful, give me a shoutout on twitter [@gavinbunney](https://twitter.com/gavinbunney)

## Development Environment

Treble is setup with a bunch of `grunt` tasks.

### Grunt Structure

The grunt structure is taken from sailsjs (props!) - it uses a directory loading system to split out the tasks into individual
JavaScript files. The tasks are contained underneath `tasks`. The `tasks/config` directory contains individual grunt task
configurations, with the `tasks/register` containing the grunt tasks available from the command line (things like `build`).

### Install bower components and node modules

```
$ bower install && bower prune
$ npm install && npm prune
```

### Quick Start

To compile, lint, test, dist and start a local server, simply run `grunt`.

```
$ grunt
```

A test server will then be available at `http://localhost:1337/`.

Running this default `grunt` task will also monitor for any file changes, then run the appropriate part of the build process.

## Distribution

After running `grunt`, a `dist` directory is created with all the files needed to deploy something with Treble. Just upload them to your favorite static host server and you're done!
