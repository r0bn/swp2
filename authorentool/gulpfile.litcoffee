    gulp =            require 'gulp'
    gutil =           require 'gulp-util'

    # server

    server =          require "gulp-develop-server"
    browserSync =     require 'browser-sync'
    reload =          browserSync.reload

    # js
    coffee =          require 'gulp-coffee'

    # html
    jade =            require 'gulp-jade'

    # css 
    stylus =          require 'gulp-stylus'

# File Creation

## HTML

    ###
    gulp.task 'html', () ->
      gulp.src './assets/jade/*.jade'
      .pipe jade()
      .pipe gulp.dest './static'
      .pipe reload({stream:true})
    ###

## JS

    gulp.task 'js', () ->
      gulp.src './assets/coffee/**/*.litcoffee'
      .pipe coffee({bare : true}).on('error', gutil.log)
      .pipe gulp.dest './static/build'
      .pipe reload({stream:true})

## CSS 

    gulp.task 'css', () ->
      gulp.src './assets/stylus/**/*.styl'
      .pipe stylus()
      .pipe gulp.dest './static/build'
      .pipe reload({stream:true})

## Build all

    gulp.task 'build', ['js', 'css'], () ->

# Watch

    gulp.task 'watch', ['build'], () ->
      gulp.watch ['assets/coffee/**/*'],['js']
      gulp.watch ['assets/stylus/**/*'],['css']
      gulp.watch ['server/views/**/*'], reload
      gulp.watch ['app.litcoffee', 'server/**/*.litcoffee'], server.restart

    gulp.task 'browser-sync', ['watch'], () ->
      browserSync {
        proxy : "localhost:4000"
        open: false
        port:4001
      }

# Server

    gulp.task 'server', () ->
      server.listen { path: 'app.litcoffee' }

    gulp.task 'default', ['server', 'browser-sync']
