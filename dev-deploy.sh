rm -r ./public/;
clj -Xbuild
npx postcss resources/*.css --dir public/css
now;

