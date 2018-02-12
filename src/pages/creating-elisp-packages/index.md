---
title: Creating elisp packages
date: "2018-02-11T12:00:00.000Z"
---
![intro](./intro.png)

Last week my first `elisp` package [elcontext](https://sojka.tech/elcontext) was
published on [melpa](https://melpa.org/#/elcontext). It was the first time I
have published a non-`JavaScript` package. Now I would like to share the
differences between the `elisp` and the `JavaScript` ecosystem I experienced.


## Learning elisp

The first step for creating a package is obviously to learn `elisp`. Resources
which helped me were the [Introduction to Programming in Emacs
Lisp](https://www.gnu.org/software/emacs/manual/html_node/eintr/index.html), the
[Emacs Lisp Reference
Manual](https://www.gnu.org/software/emacs/manual/html_node/elisp/index.html)
and [Practical Emacs Lisp](http://ergoemacs.org/emacs/elisp.html). A big
difference to my usual `JavaScript` development experience was Emacs' built-in
help system. I discovered many useful `elisp` snippets in other packages by
browsing through their help buffers and sources. It is important to know many
`elisp` packages and to learn from their code since there are fewer online
resources about `elisp` compared to `JavaScript`.

Writing lisp was intimidating at first but
[paredit](https://melpa.org/#/paredit) was a great help to stay sane with hordes
of parentheses. The [Animated Guide to
Paredit](http://danmidwood.com/content/2014/11/21/animated-paredit.html) and
[Emacs Rocks!](http://emacsrocks.com/e14.html) are great introductions for
paredit. Today I cannot imagine writing any code (including `JavaScript`)
without slurping & barfing.

![melpa](./melpa.png)

After writing [elcontext](https://github.com/rollacaster/elcontext) I published
it on [melpa](https://melpa.org/#/) which is `elisp`'s equivalent to
[npm](https://www.npmjs.com/). Instead of a one simple `npm publish` you have to
perform several steps for melpa. First, you need to use
[package-lint](https://github.com/purcell/package-lint) which informs you about
common issues of your package. Then, you must run `M x checkdoc` which verifies
a consistent usage of documentation strings. The steps before opening a PR
to melpa are to ensure your code byte-compiles cleanly and to test whether you
are able to install your package by using melpa locally. Now you can create a
recipe that looks similar to this one:

```emacs-lisp
(elcontext :repo "rollacaster/elcontext"
           :fetcher github)
```

After submitting your PR, melpa's maintainers review your code and suggest some
improvements. I learned a lot about `elisp` by applying the reviews' comments
and I checked [some other PRs](https://github.com/melpa/melpa/pulls) on melpa to learn even more.

Adding a package to melpa is definitely more work compared to npm but I am also
much more confident about the quality of my package.


## Conclusion

Getting out of my usual `JavaScript` development was a valuable lesson. Looking
at other programming languages and communities seems to be one of the fastest
ways to evolve as a programmer. I hope I will be doing this at least once a
year.

###### Attributions
- <sup>"[Emacs Icon](https://www.gnu.org/software/emacs/manual/html_node/eintr/index.html)" by [Nicolas Petton](https://nicolas.petton.fr/), used under [CC BY-SA 4.0](https://creativecommons.org/licenses/by-sa/4.0/)</sup>
- <sup>"[Packaging Icon](http://www.softicons.com/business-icons/ecommerce-and-business-icons-by-designcontest.com/packaging-icon)" by [DesignContest.com](http://www.softicons.com/designers/designcontest.com), used under [CC BY](https://creativecommons.org/licenses/by/3.0/)</sup>

