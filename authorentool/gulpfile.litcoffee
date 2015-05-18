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

    # documentation
    globby =          require 'globby'
    exec =            require('child_process').exec


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
      .pipe concat 'main.js'
      .pipe gulp.dest './static/build'
      .pipe reload({stream:true})
      

    gulp.task 'js-vendor', () ->
        gulp.src [    

            './assets/js/google2.api.js'

            './bower_components/codemirror/lib/codemirror.js'
            './bower_components/codemirror/mode/xml/xml.js'
            './bower_components/codemirror/addon/fold/*.js'
            './bower_components/angular/angular.js'
            './bower_components/angular-route/angular-route.js'

            './assets/js/vis.min.js'

            './bower_components/angular-ui-codemirror/ui-codemirror.js'
            './bower_components/jquery/dist/jquery.min.js'
            './bower_components/bootswatch-dist/js/bootstrap.min.js'  
            './bower_components/bootswatch-dist/js/bootstrap-lightbox.min.js'
            './assets/js/jQueryUI/jquery-ui.min.js'
            
        ]
        .pipe concat 'vendor.js'
        #.pipe uglify()
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
            './bower_components/codemirror/addon/fold/foldgutter.css'
            './bower_components/codemirror/theme/eclipse.css'
            './bower_components/bootswatch-dist/css/bootstrap.css'
            './assets/js/vis.min.css'
            './assets/js/jQueryUI/jquery-ui.min.css'
        ]
        .pipe concat 'vendor.css'
        .pipe gulp.dest './static/build'
        .pipe reload({stream:true})


## Documentation

    gulp.task 'docs', () ->
        globby ["README.md", "assets/**/*", "gulpfile.litcoffee"], (err, files) ->
            fileList = files.join(" ")

            exec __dirname + "/node_modules/docco/bin/docco -o docs/ -L " +__dirname + "/docs/language.json -l linear " + fileList, (err, stdout, stderr) ->
                console.log stdout
                console.log stderr


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
