rm -r ./public/;
cp -r ./models ./public/;
emacs --script publish.el;
npx purgecss -css resources/syntax-highlighting.css --content public/setup-tests-with-cider-and-depsedn-in-5-steps.html -o public/css/syntax-highlighting.css
clj src/tech_thomas_sojka/core.clj;
shadow-cljs release app;
now;
