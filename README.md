Pensieve puzzle hunt
====================

This service is tested on Java 11 and Yarn 1.19.1.

### Quickstart

    $ cd pensieve-puzzle-hunt
    $ yarn && yarn build  # builds frontend assets
    $ ./gradlew run  # starts server

Visit `localhost:8090`.

### Development

The Java backend files is in `src/`, and the frontend Javascript files are in `web/`.

    $ ./gradlew eclipse  # sets up Eclipse project
    $ yarn start  # runs Webpack dev server

