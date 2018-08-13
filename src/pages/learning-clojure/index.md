---
title: Learning clojure
date: "2018-08-13T12:00:00.000Z"
---
One of my good intentions this year is to learn a new programming language. Choosing between the various options proved to be a difficult task. I tried to choose between PureScript, Clojure and Reason ML. In the end the article about the [State of Clojure in 2018](http://blog.cognitect.com/blog/2017/1/31/clojure-2018-results) was the decisive factor. 50% of all respondents were Emacs users and since my latest [shenanigans with Emacs Lisp](https://github.com/rollacaster/elcontext) I was sold with lisp anyway.

If you search for Clojure learning resources most roads lead to [BRAVE CLOJURE](https://www.braveClojure.com/), an excellent book introducing Clojure. Thanks to this book I will skip the Clojure basics in this post (just read the book 😉) and start with my Clojure journey right after finishing the book.

When I look back on how I learned other programming languages, I always regret that I didn't read enough code from other people. Especially in my main language, JavaScript, I feel uncomfortable reading library code on github. This has changed for me with Clojure and I think this is due to Lisp's minimal syntax approach. There is less personal style involved when reading other Clojure code besides `(some-fn some-args)` thus there are not that many other options to write your code or at least less options than in JavaScript.

I started my Clojure journey by finding some relevant libraries and skimming through the code these libraries included:

-   core-async
-   reagent
-   re-frame
-   ring
-   compojure
-   nrepl
-   figwheel
-   leiningen
-   secretary

I haven't read every library thoroughly, but just enough to get the general implementation of the library and learn some new tricks with Clojure.

I started my first Clojure project a shareable shopping list which is still work in progress. After my initial Clojure honeymoon phase the first issues arose. There are many different profiles to bootstrap your application with `leiningen` but it was difficult to adjust anything with the predefined setups. Every time I fixed a problem with my setup, I created a new one. In the end I removed most of the code from the profile and did the dev setup myself, which took some time, but now everything works as expected.

In general I think it was a good choice to learn Clojure, the interactive development approach with Clojure's REPL allows short feedback loops when developing an application. In addition you notice the long design phase of Clojure most of core methods are composable and intuitive to use. I hope more blog posts about Clojure will follow soon.

