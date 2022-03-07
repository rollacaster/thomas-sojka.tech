(ns tech.thomas-sojka.org-parser-hiccup
  (:require [clojure.string :as str]
            [hiccup2.core :as hiccup]
            [org-parser.parser :as parser]))

(defn link-ext-file [[ext-file]]
  (-> ext-file
      (str/replace "file:" "")
      (str/replace "org" "html")))

(defn link-ext-others [parts]
  (let [[[_ scheme] [_ rest]] parts]
    (str scheme ":" rest)))

(defn link [link-parts]
  (let [[[_ [_ [link-ext-type & link-ext]]]
         [_ description]]
        link-parts]
    (if (str/includes? (first link-ext) "png")
      [:img {:src (first link-ext) :alt description}]
      [:a {:href (case link-ext-type
                   :link-ext-file
                   (link-ext-file link-ext)
                   :link-ext-other (link-ext-others link-ext))}
       description])))

(defn text [text-parts]
  (map
   (fn [[text-type & text-contents]]
     (case text-type
       :text-normal (first text-contents)
       :text-sty-verbatim [:code (first text-contents)]
       :link-format (link text-contents)))
   text-parts))

(defn content [contents]
  (map
   (fn [[content-type & contents]]
     (cons
      :p
      (case content-type
        :text (text contents))))
   contents))

(defn parse-keyword [[[_ key] [_ value]]]
  (when (= (str/lower-case key) "title")
    [:h1 value]))

(defn list-item-line [list-item-line-parts]
  (->> list-item-line-parts
       (map
        (fn [[type & contents]]
          (case type
            :text (text contents)
            nil)))
       (remove nil?)
       (map #(cons :li %))
       first
       vec))

(defn src-block [blocks]
  [:pre
   (->> blocks
        (map second)
        (str/join "\n"))])

(defn raw-text [[[_ & text-parts]]]
  (->> text-parts
       (map (fn [[type content]]
              (case type
                :text-sty-verbatim (str "=" content "=")
                content)))
       (str/join)))

(defn parse [org-path]
  (->> (parser/parse (slurp org-path))
       (drop 1)
       (reduce
        (fn [hiccup [type & remaining]]
          (case type
            :keyword-line (conj hiccup (parse-keyword remaining))
            :affiliated-keyword-line (conj hiccup (parse-keyword remaining))
            :drawer-begin-line (conj hiccup :drawer-begin-line)
            :drawer-end-line (vec (drop-last hiccup))
            :content-line (let [prev (last hiccup)]
                            (cond
                              (= prev :drawer-begin-line) hiccup
                              (= (first prev) :src-block)
                              (conj
                               (vec (drop-last hiccup))
                               (apply conj prev (content remaining)))
                              (= (first prev) :export-block)
                              (conj
                               (vec (drop-last hiccup))
                               (conj prev (raw-text remaining)))
                              :else (apply conj hiccup (content remaining))))
            :block-begin-line (let [[[_ block-name]] remaining]
                                (case (str/lower-case block-name)
                                  "src" (conj hiccup [:src-block])
                                  "export" (conj hiccup [:export-block])))
            :block-end-line (let [[type & remaining] (last hiccup)]
                              (case type
                                :src-block (conj (vec (drop-last hiccup)) (src-block remaining))
                                :export-block (conj (vec (drop-last hiccup)) [:raw (str/join remaining)])))
            :headline (let [[[_ stars] [_ [_ title]]] remaining]
                        (conj hiccup [(keyword (str "h" (inc (count stars)))) title]))
            :list-item-line
            (let [prev (last hiccup)
                  [prev-type] prev]
              (cond
                (= prev-type :ul)
                (conj
                 (vec (drop-last hiccup))
                 (conj prev (list-item-line remaining)))
                (not= prev-type :li)
                (conj hiccup [:ul (list-item-line remaining)])))
            hiccup))
        [])
       (remove nil?)
       (map (fn [[type :as element]]
              (case type
                :raw (hiccup/raw (second element))
                (vec element))))))

(comment
  (def org-path "content/elcontext.org")

  (->> (parse org-path)
       (map (fn [el] (hiccup/html el)))
       str/join
       (spit "public/test.html"))


  (= (parse "content/6-tips-to-finish-your-side-project.org")
     "<h1>6 Tips to finish your Side Project</h1><p>I fail to finish about 60% of my side projects but recently I finished six projects in a row. This article explains how I did this.</p><p>In the last five years I started 41 side projects and only finished 17. To me, finishing a side project means that I successfully executed my idea and after that I published it  by writing a blog about it or if I built something for another person and I send it to this person.</p><p>As you can see in the chart below lately my success rate peaked, so I reflected about recent changes how I approached my side projects.</p><picture>  <source media=\"(max-width: 799px)\" srcset=\"images/side-projects.svg\">  <source media=\"(min-width: 800px)\" srcset=\"images/side-projects-lg.svg\">  <img src=\"images/side-projects.png\" alt=\"Chart displaying all my succeeded/failed side projects\"></picture><p>I identified 6 tips which could help you too. Of course, these tips are representing my journey and not all of them will work you. Try to pick one or two and test for yourself whether they can help you.</p><h3>Tips</h3><h4>Stop when it&apos;s fun</h4><p>Here&apos;s something that happens regularly to me: I finish a challenging task and I am so excited about it that I tackle the next one immediately. I avoid this now because what usually happens is that I fail to finish the next task. I guess it happens because I didn&apos;t notice being exhausted after finishing the challenging task. Then, after I failed the next task I have to stop working being frustrated which makes it harder to get back to it.</p><p>Instead, now I stop working after successfully finishing a task. This way I stop with a good feeling and I am curious to start the next task which makes it easy to continue with the project.</p><h4>Prototype early &amp; often</h4><p>After you finished your first task you should immediately think about publishing it, even if it&apos;s just a &apos;Hello World&apos;. Start to build a minimal Deployment Pipeline which allows you to share the current state of your project. Even if your project looks boring in the beginning take some screenshots so that you continuously see your progress.</p><p>If your side project is an application hosted on the web, https://vercel.com is a great way to setup your deployment pipeline within a few  minutes.</p><h4>Plan 2-4 steps ahead</h4><p>Deciding completely independent what to do next is my greatest joy when working on a side project. But with great power comes great responsibility. It happened quite often that I lost focus of my overall goal. I spend too much time with details without any major progress. This usually results in an unfinished side project.</p><p>To avoid this I always maintain a list with 2-4 next steps. Each step is so small that it takes about half an hour. Planning not too far ahead helps to balance between spontaneously adjusting your project to new ideas while focusing on your main goal.</p><h4>Track your time</h4><p>I started to track the time of each task I do. This helps me to notice when I am stuck and allows me to change my plan. For instance, if I notice I tried to solve a task for more than 30 minutes without any progress, it&apos;s worth thinking about other approaches. In addition, it&apos;s really interesting to know how much time you spent with your side project and you continuously get better at estimating how much time you need to realize new ideas. For instance writing this blog including the creation of the data visualisation above took me about 7 hours.</p><p>I am using <a href=\"https://orgmode.org/manual/Clocking-Work-Time.html\">Org Mode&apos;s Clocking Feature</a> to track my time but I am sure there are plenty of tools which can help you to track your time which are not dependent on a certain editor. Unfortunately I do not know any other tools.</p><h4>Do multiple side projects at once</h4><p>I am sure this tip does not work for everyone but it helped me a lot. I usually have 2-4 side projects at once and I work on 1-2 of them each day for about half an hour. I am sure there are people who need to focus on one topic to be productive. For me, if I try to work on the same thing each day it gets boring. It&apos;s probably best to find your own way by trying different approaches.</p><h4>Take your time</h4><p>Even if you follow all of the tips and read through many other great resources about this topic there is nothing more valuable than gathering your own experiences. It took me five years and 41 projects until I found a workflow which seems to work for me. Hopefully my tips can help you reach this goal quicker than I did.</p><p>If you have any other tips which help to finish a side project please reach out: <a href=\"https://mobile.twitter.com/rollacaster\"></a></p>"
     )
  (=
   (parse "content/100-days-of-spaced-repetition.org")
   "<h1>100 Days of Spaced Repetition</h1><p>100 days ago I was introduced to Spaced Repetition, a learning method scientifically proven to work. The basic idea is to learn facts and repeat them at increasing intervals. After several iterations, you remember the fact forever.</p><p><img alt=\"file:~/projects/thomas-sojka-tech/src/images/spaced-repetition.png\" src=\"images/spaced-repetition.png\" /></p><p>I was introduced to Spaced Repetition by reading <a href=\"https://ncase.me/remember/\">How To Remember Anything Forever-ish</a> by Nicky Case. It is a great introduction because you will begin your Spaced Repetition adventure while you are still reading. After you finish the article you will know what Spaced Repetition is, how it works, why it works and you will have completed your first session.</p><p>In this article, I want to share my experience 100 days after reading the article and I can say this much in advance: you really should try it yourself.</p><h2>Why I thought Spaced Repetition is a bad idea 🤔</h2><p>Although I immediately started with Spaced Repetition after reading the article, I had some doubts about whether it was really useful. There is simply no need to memorize everything when you have a smartphone in your pocket with all possible facts only fingertips away. Still, I wanted to try it for a few weeks, learning 10 facts a day. This was enough to change my mind.</p><h2>Which benefits changed my mind  🤯</h2><h3>Staying in the flow ⌛</h3><p>I work as a programmer and need to remember many function names. With Spaced repetition instead of opening a browser and google for a function name, I can create a new fact each time I forget one. After some time I can stay in the flow of my current work for a long time.</p><p>Besides function names, other things interrupt my workflow, such as forgetting mathematical conversions or having to recalculate constants (e.g. how many seconds are in a day). I am sure that in every profession there is some information that you constantly recheck and getting rid of this is a soothing experience.</p><h3>Keep valuable knowledge 🗝️</h3><p>Before I did Spaced Repetition I felt that all that mattered was knowing where to find information and that keeping it all in your memory was bad because of limited memory space. I still think that for some information it is bad to keep them in your memory, e.g. unfinished tasks that you are afraid of forgetting should be written down immediately because a lot of stress vanishes when you note them down. But while it&apos;s stressful to keep certain information in your brain, I no longer feel like memory space is limited in any way. As long as you memorize new things at a steady and fairly slow pace, it feels like you can go on forever.</p><p>I also noticed another change. Whenever I read something, I am constantly searching for relevant facts I can use for Spaced Repetition in the back of my mind. This makes my readings more valuable because I can be sure not to forget anything important and scanning texts for relevant facts helps me to better understand the text itself.</p><h3>Think outside the box 📦</h3><p>There are some topics that I deem important to know but have always been too lazy to learn. For example, how my country&apos;s political system works, understanding the effects of global warming in concrete numbers, or current relevant events like understanding how the Covid-19 vaccine works.</p><p>These topics or pretty much any topic where I have no prior knowledge or forgotten everything I learned in school would have been intimidating to learn for me. Now, with Spaced Repetition I can learn those topics slowly e.g. by starting with three definitions about the topic and building up upon those facts which seems less scary to me. Learning new subjects slowly is much easier for me and I take pride in acquiring the knowledge I think is important.</p><h2>What surprised me in the last 100 days 😮</h2><p>My daily recap time is not constantly increasing. I expected that as I added more facts per day, it would take me more and more time to recap my facts. But currently it takes me about 15-20 minutes each day and it&apos;s not getting any longer.</p><p>Boring facts are hard to remember. Whenever I have trouble remembering a fact, I realize that the fact is not interesting to me anymore. It&apos;s really important to add facts that interest you to facilitate learning.</p><p>One thing which was frustrating at times was finding the material for my 10 daily facts. This struggle felt like a constant rollercoaster, in the beginning, it was easy and sometimes I would prepare more than 10 facts and be able to save them for the next day. Other days, I would spend 40 minutes and end up with new facts that I wasn&apos;t interested in. I am confident I will improve and hopefully my blog post &quot;A Year of Spaced Repetition&quot; will be full of good advice on how to find facts that are relevant to you.</p><p>Another surprise was my renewed interest in old topics which had not interested me for a while. I studied information systems, which is a mixture of computer science and economics, but after university, I became completely immersed in programming and haven&apos;t learned anything related to economics in years. With Spaced Repetition, it was easy to get back into it because I could relearn it in small steps, which was is a nice experience.</p><p>At the end of <a href=\"https://ncase.me/remember/\">How To Remember Anything Forever-ish</a> was a link to <a href=\"https://www.supermemo.com/en/archives1990-2015/articles/20rules\">Effective learning: Twenty rules of formulating knowledge</a> and this post keeps what it promises. After learning how to properly formulate my facts, learning became easier. But I would advise against reading it when you&apos;re just starting with Spaced Repetition. In the beginning, forming a habit is much more important than proper technique but you should save this link and read it a few weeks later.</p><h2>What I learned 💡</h2><p>I&apos;d like to share with you the areas I have learned new facts in to give you some ideas for your learning journey. Of course, each journey is different and ultimately everyone has to find their own path but those categories might help you discover some areas of interest.</p><h3>Order of magnitudes ⚖️</h3><p>Sometimes I read about a number and I cannot interpret it because I lack the context to understand it. What I try to do now is to learn some numbers of certain areas e.g. revenue:</p><ul><li>What&apos;s the revenue of Apple?</li><li>What&apos;s the revenue of BMW?</li><li>What&apos;s the revenue of Spotify?</li></ul><p>After learning these numbers, whenever someone tells me the revenue of a new company, I can compare it and understand if this number is high or low. Some other magnitudes that I learned this way:</p><ul><li>Data processing speed</li><li>Internet speed</li><li>Planet sizes</li><li>GDP of countries</li><li>Areas of countries</li></ul><h3>Current job 👨‍💻</h3><p>I have worked as a programmer for five years and studied information systems before that. That&apos;s plenty of time to forget a lot of the basics I learned at university but Spaced repletion allowed me to recap:</p><ul><li>Data structures and how they work</li><li>Algorithms and how they work</li><li>Which subfields there are in computer science</li><li>The winners of the Turing Award</li></ul><h3>New job areas 📊</h3><p>For me, data visualization is an area where I want to dive further and I have been using Spaced Repetition to learn the basics:</p><ul><li>What kind of data visualizations exist?</li><li>What are the advantages/disadvantages of certain data visualizations?</li><li>What are the key principles to create a data visualization?</li></ul><h3>Books 📚</h3><p>Books are full of knowledge worth keeping and it&apos;s always sad when that knowledge fades away after some years. With Spaced Repetition you can stop this. I have summarized facts from:</p><ul><li>The pragmatic programmer</li><li>Refactoring UI</li><li>Drive</li><li>The 7 habits of highly effective people</li><li>Conversationally speaking</li><li>To Sale is Human</li></ul><h3>General and recent knowledge 🤓</h3><p>By starting with minor facts learning doesn&apos;t feel overwhelming anymore. This way</p><p>There are so many things that I always thought I should know, but starting to learn those felt overwhelming. By starting with minor facts learning doesn&apos;t feel overwhelming anymore. This way I learned:</p><ul><li>The effects of climate change</li><li>How Covid-19 vaccination works</li><li>How the German and European political systems works</li></ul><h2>Conclusion 🎇</h2><p>I am looking forward to the next 100 days of Spaced Repetition and I am curious if I am going to use it differently than I am using it now. I hope this article got you some ideas about why you should use Spaced Repetition. If you&apos;re already using it maybe you found some inspiration about additional things to learn.</p><p>I would be happy to receive feedback or ideas about this topic. You can contact me by <a href=\"mailto:contact@thomas-sojka.tech\">mail</a> or <a href=\"https://mobile.twitter.com/rollacaster\">Twitter</a>.</p>"
   )
  (= (parse "content/about.org")
     "<h1>Thomas Sojka</h1><figure id=\"org32f227f\" style=\"margin-top:1em;margin-bottom:0;\">  <a href=\"images/me.png\" class=\"hidden md:block\"><img src=\"images/me.png\" alt=\"Picture of Thomas Sojka\" style=\"float: left;margin-right: 3rem;border-radius: 0.25rem;\" ></a></figure><p>Hi 👋</p><p>My name is Thomas, I am a programmer living in Munich.</p><p>I am working at <a href=\"https://comsystoreply.de/\">comSysto</a> where I write business applications for <a href=\"https://comsystoreply.de/referenzen\">various projects</a>. Before that I studied Information Systems at <a href=\"https://www.tum.de/en/\">TUM</a>.</p><p>I am interested in <a href=\"https://www.youtube.com/playlist?list=PLB3sLatZtqYms9T85gf_PTyneg1SLvsEa\">Data</a> <a href=\"https://medium.com/nightingale/steal-like-a-data-visualiser-2ec7fb470896?source=friends_link&amp;sk=8ab6fa936d6e61dbdec2c2a7f607d1a0\">Viz</a>, <a href=\"https://www.youtube.com/watch?v=juMLwOTxnvw\">Functional</a> <a href=\"https://www.youtube.com/watch?v=juMLwOTxnvw\">Programming</a> and <a href=\"https://rollacaster.github.io/sketches/\">art</a> <a href=\"https://twitter.com/rollacaster/status/1351486650992439296\">with</a> <a href=\"https://fire-hands.now.sh/\">code</a>.</p><p>You can find me on <a href=\"https://twitter.com/rollacaster\">Twitter</a>, <a href=\"https://github.com/rollacaster\">GitHub</a>, <a href=\"https://www.youtube.com/channel/UCBSMA2iotgxbWPSLTFeUt9g\">YouTube</a> or contact me via <a href=\"mailto:contact@thomas-sojka.tech\">mail</a>.</p><figure id=\"org32f227f\">  <a href=\"images/me.png\" class=\"md:hidden\" alt=\"Picture of Thomas Sojka\"><img src=\"images/me.png\" alt=\"Picture of Thomas Sojka\" style=\"margin-top:3rem;border-radius: 0.25rem;\"></a></figure>"
     )
  (= (parse "content/creating-elisp-packages.org")
     "<h1>Creating elisp packages</h1><p><img src=\"intro.png\" /></p><p>Last week my first <code>elisp</code> package <a href=\"elcontext.html\">elcontext</a> was published on <a href=\"https://melpa.org/#/elcontext\">melpa</a>. It was the first time I have published a non-<code>JavaScript</code> package. Now I would like to share the differences between the <code>elisp</code> and the <code>JavaScript</code> ecosystem I experienced.</p><h3>Learning elisp</h3><p>The first step for creating a package is obviously to learn <code>elisp</code>. Resources which helped me were the <a href=\"https://www.gnu.org/software/emacs/manual/html_node/eintr/index.html\">Introduction to Programming in Emacs Lisp</a>, the <a href=\"https://www.gnu.org/software/emacs/manual/html_node/elisp/index.html\">Emacs Lisp Reference Manual</a> and <a href=\"http://ergoemacs.org/emacs/elisp.html\">Practical Emacs Lisp</a>. A big difference to my usual <code>JavaScript</code> development experience was Emacs’ built-in help system. I discovered many useful <code>elisp</code> snippets in other packages by browsing through their help buffers and sources. It is important to know many <code>elisp</code> packages and to learn from their code since there are fewer online resources about <code>elisp</code> compared to <code>JavaScript</code>.</p><p>Writing lisp was intimidating at first but <a href=\"https://melpa.org/#/paredit\">paredit</a> was a great help to stay sane with hordes of parentheses. The <a href=\"http://danmidwood.com/content/2014/11/21/animated-paredit.html\">Animated Guide to Paredit</a> and <a href=\"http://emacsrocks.com/e14.html\">Emacs Rocks!</a> are great introductions for paredit. Today I cannot imagine writing any code (including <code>JavaScript</code>) without slurping &amp; barfing.</p><p><img src=\"melpa.png\" /></p><p>After writing <a href=\"elcontext.html\">elcontext</a> I published it on <a href=\"https://melpa.org/#/\">melpa</a> which is elisp’s equivalent to <a href=\"https://www.npmjs.com/\">npm</a>. Instead of a one simple <code>npm publish</code> you have to perform several steps for melpa. First, you need to use <a href=\"https://github.com/purcell/package-lint\">package-lint</a> which informs you about common issues of your package. Then, you must run <code>M x checkdoc</code> which verifies a consistent usage of documentation strings. The steps before opening a PR to melpa are to ensure your code byte-compiles cleanly and to test whether you are able to install your package by using melpa locally. Now you can create a recipe that looks similar to this one:</p><pre>(elcontext :repo &quot;rollacaster\n           :fetcher github)</pre><p>After submitting your PR, melpa’s maintainers review your code and suggest some improvements. I learned a lot about <code>elisp</code> by applying the reviews’ comments and I checked <a href=\"https://github.com/melpa/melpa/pulls\">some other PRs</a> on melpa to learn even more.</p><p>Adding a package to melpa is definitely more work compared to npm but I am also much more confident about the quality of my package.</p><h3>Conclusion</h3><p>Getting out of my usual <code>JavaScript</code> development was a valuable lesson. Looking at other programming languages and communities seems to be one of the fastest ways to evolve as a programmer. I hope I will be doing this at least once a year.</p><h4>Attributions</h4><ul><li>”<a href=\"https://www.gnu.org/software/emacs/manual/html_node/eintr/index.html\">Emacs Icon</a>” by <a href=\"https://nicolas.petton.fr/\">Nicolas Petton</a>, used under <a href=\"https://creativecommons.org/licenses/by-sa/4.0/\">CC BY-SA 4.0</a></li><li>”<a href=\"http://www.softicons.com/business-icons/ecommerce-and-business-icons-by-designcontest.com/packaging-icon\">Packaging Icon</a>” by <a href=\"http://www.softicons.com/designers/designcontest.com\">DesignContest.com</a>, used under <a href=\"https://creativecommons.org/licenses/by/3.0/\">CC BY</a></li></ul>"
     ))



