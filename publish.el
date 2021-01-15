(package-initialize)
(require 'org)
(require 'ox)
(require 'ox-html)
(require 'ox-publish)
(require 'htmlize)

(setq org-export-allow-bind-keywords t)
(setq org-html-preamble-format
      '(("en"
         "<header>
            <h1 class=\"head\"><a href=\"/\">Thomas Sojka</a></h1>
          </header>")))
(setq org-html-postamble-format
      '(("en"
         "<footer>
            <div>
              <a href=\"https://mobile.twitter.com/rollacaster\">Twitter</a>
              <a href=\"https://github.com/rollacaster\">GitHub</a>
              <a href=\"https://www.youtube.com/channel/UCBSMA2iotgxbWPSLTFeUt9g?view_as=subscriber\">YouTube</a>
            </div>
          </footer>")))
(setq org-publish-project-alist
      '(("org-notes"
         :base-directory "~/projects/thomas-sojka-tech/src"
         :base-extension "org"
         :publishing-directory "~/projects/thomas-sojka-tech/public/"
         :recursive t
         :publishing-function org-html-publish-to-html)
        ("org-static"
         :base-directory "~/projects/thomas-sojka-tech/src"
          :base-extension "css\\|js\\|png\\|jpg\\|gif\\|pdf\\|mp3\\|ogg\\|svg\\|swf"
          :publishing-directory "~/projects/thomas-sojka-tech/public/"
          :recursive t
          :publishing-function org-publish-attachment)
        ("rss"
         :base-directory "~/projects/thomas-sojka-tech/src"
         :base-extension "org"
         :html-link-home "https://thomas-sojka.tech/"
         :html-link-use-abs-url nil
         :rss-extension "xml"
         :publishing-directory "~/projects/thomas-sojka-tech/public/"
         :publishing-function (org-rss-publish-to-rss)
         :section-numbers nil
         :exclude ".*"            ;; To exclude all files...
         :include ("index.org")   ;; ... except index.org.
         :table-of-contents nil)
         ("website" :components ("org-notes"  "org-static" "rss"))))
(org-publish-all t)
