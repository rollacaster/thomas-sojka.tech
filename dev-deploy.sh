rm -r ./public/;
emacs --script publish.el;
clj src/tech_thomas_sojka/core.clj;
shadow-cljs release app;
now;
