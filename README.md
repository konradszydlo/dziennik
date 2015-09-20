# dziennik

Sample application where you can manage school stuff like teachers, students etc.

## Overview

To run use:

    lein run

Go to http://localhost:3000

Click on 'View all teachers' to see a few react components that display teachers, details of particular teacher.

You can add students to teacher's list.

## Setup

The app uses datomic.

Download datomic (free) from: http://www.datomic.com/get-datomic.html

Once downloaded and extracted You will need to add the Datomic's folder and lein-datomic plugin to lein's profile e.g.

     {:user  {:plugins [[lein-datomic "0.2.0"]
                   [cider/cider-nrepl "0.9.1"]]
         :datomic {:install-location "/home/your-user-name/datomic-free-0.9.5078"}}}




To get an interactive development environment run:

    lein figwheel

and open your browser at [localhost:3449](http://localhost:3449/).
This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL. An easy way to try it is:

    (js/alert "Am I connected?")

and you should see an alert in the browser window.

To clean all compiled files:

    lein clean

To create a production build run:

    lein cljsbuild once min

And open your browser in `resources/public/index.html`. You will not
get live reloading, nor a REPL.

## To Do

  - Add more data e.g. teachers, students
  - Add support for classes
  - Add test scores for students
  - Add historical data for students across different years
  - Datomic's data are hardcoded - use SQL storage

## License

Copyright © 2015

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
