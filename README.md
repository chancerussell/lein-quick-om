# lein-quick-om

A quick Leiningen template for simple [Om](https://github.com/omcljs/om)-based applications.

Alpha-quality, provided with no warranty.

## Features

### "devbar"

A bootstrap navbar for use in development. The component will add itself as the
first child div in the document body. You can pass a list of functions and
labels to automatically generate buttons to use as development shortcuts:

```clojure
(PROJ_NAME.devbar/add-dev-bar APP_STATE
  {:buttons [[reset-state "reset state"]
             [shutdown-json-refresh "stop JSON reloading"]
             [#(reset! background-color "blue") "make background blue]]})
```

### State viewer

A component for monitoring an Om state hashmap, using
[Ankha](https://github.com/noprompt/ankha). By default, the viewer will create
a popup window  and render itself there.

TODO: Functionality for automatically dealing with objects Ankha doesn't know
how to render.

### Dockerfile/Makefile
The included Dockerfile can be used to run your application in isolation. The
following commands *should* bring up a running system:

```bash
make dbuild
make figwheel
```

The `figwheel` make task automatically mounts the `src` and `resources`
directories in the running container, so changes to your code will be picked up
by the containerized server.

The `quick` target is the same as `figwheel`, except it will also mount the
`local-m2` dependency directory. This speeds up container start time, at the
expense of some reproducibility.

You can specify container and host port numbers with `CONTAINER_PORT` and
`HOST_PORT`. 

