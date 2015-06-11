docker run -it --rm --name authorentool -v "$PWD":/usr/src/authorentool -w /usr/src/authorentool -p 4001:4001 node:latest node ./node_modules/gulp/bin/gulp.js 
