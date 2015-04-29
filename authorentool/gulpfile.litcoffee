    gulp =            require 'gulp'
    gutil =           require 'gulp-util'
    order =           require 'gulp-order'

    ftp =             require 'gulp-ftp'
    try
        ftpConfig =       require './config-ftp.json'
    catch
        console.log "task deployment might requires credentials"

    # server
    browserSync =     require 'browser-sync'
    reload =          browserSync.reload

    # js
    coffee =          require 'gulp-coffee'
    uglify =          require 'gulp-uglify'
    concat =          require 'gulp-concat'

    # html
    jade =            require 'gulp-jade'

    # css 
    stylus =          require 'gulp-stylus'

# File Creation

## HTML

    gulp.task 'html', () ->
      gulp.src './assets/jade/*.jade'
      .pipe jade()
      .pipe gulp.dest './static'
      .pipe reload({stream:true})

## JS

    gulp.task 'js', () ->
      gulp.src './assets/coffee/**/*.litcoffee'
      .pipe coffee({bare : true}).on('error', gutil.log)
      .pipe gulp.dest './static/build'
      .pipe reload({stream:true})

    gulp.task 'js-vendor', () ->
        gulp.src [
            './bower_components/codemirror/lib/codemirror.js'
            './bower_components/codemirror/mode/xml/xml.js'
            './bower_components/angular/angular.js'
            './bower_components/angular-ui-codemirror/ui-codemirror.js'
            './bower_components/jquery/dist/jquery.min.js'
            './bower_components/bootswatch-dist/js/bootstrap.min.js'  
        ]
        .pipe concat 'vendor.js'
        .pipe uglify()
        .pipe gulp.dest './static/build'
        .pipe reload({stream:true})

## CSS 

    gulp.task 'css', () ->
      gulp.src './assets/stylus/**/*.styl'
      .pipe stylus()
      .pipe gulp.dest './static/build'
      .pipe reload({stream:true})

    gulp.task 'css-vendor', () ->
        gulp.src [
            './bower_components/codemirror/lib/codemirror.css'
            './bower_components/codemirror/theme/eclipse.css'
            './bower_components/bootswatch-dist/css/bootstrap.css'
        ]
        .pipe concat 'vendor.css'
        .pipe gulp.dest './static/build'
        .pipe reload({stream:true})

## Build all

    gulp.task 'build', ['js', 'css', 'html', 'js-vendor', 'css-vendor'], () ->

## Deployment

    gulp.task "deploy", ['build'], () ->
        gulp.src "static/**/*"
            .pipe ftp {
                host : ftpConfig.host
                user : ftpConfig.user
                pass : ftpConfig.pass
                remotePath : "/"
            }

# Watch

    gulp.task 'watch', ['build'], () ->
      gulp.watch ['assets/coffee/**/*'],['js']
      gulp.watch ['assets/stylus/**/*'],['css']
      gulp.watch ['assets/jade/**/*'], ['html']

    gulp.task 'browser-sync', ['watch'], () ->
      browserSync {
        server :
            baseDir : "./static"
        open: false
        port:4001
      }

# Server

    gulp.task 'default', ['browser-sync']
