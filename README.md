# shadow-cljs re-frame example

A simple `re-frame` template
.

### Install Javascript dependencies
```
yarn
```

### Run shadow-cljs dev server
```
npx shadow-cljs watch app

open http://localhost:8280
```

### Run tests

[Interactive](https://shadow-cljs.github.io/docs/UsersGuide.html#target-browser-test):

To run tests with `shadow-cljs` when files change:
```
npx shadow-cljs watch browser-test

open http://localhost:8290
```

[karma](https://shadow-cljs.github.io/docs/UsersGuide.html#target-karma):

For a single run appropriate for a CI environment:
```
yarn test
```

### Clojurescript REPL

If you have the app watch task running (`npx shadow-cljs watch app`), you should be able to connect to its nREPL server at `localhost:8230`.

Once connected, `dev/user.clj` should load automatically, exposing a `cljs-repl` function we've defined in the `user` namespace. 

This is basically just an alias for:

```clj
(require '[shadow.cljs.devtools.api :as dapi])
(dapi/nrepl-select :app)
```

Evaluating `(cljs-repl)` should start the Clojurescript REPL for the `:app` build.
