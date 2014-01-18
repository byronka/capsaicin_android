REM
REM        Usage:
REM        android [global options] create test-project [action options]
REM        Global options:
REM   -h --help       : Help on a specific command.
REM   -v --verbose    : Verbose mode, shows errors, warnings and all messages.
REM      --clear-cache: Clear the SDK Manager repository manifest cache.
REM   -s --silent     : Silent mode, shows errors only.
REM 
REM                               Action "create test-project":
REM   Creates a new Android project for a test package.
REM Options:
REM   -p --path    : The new project's directory. [required]
REM   -n --name    : Project name.
REM   -m --main    : Path to directory of the app under test, relative to the test
REM                  project directory. [required]

set test_global_options=--verbose
set test_action_options=--path ./capsaicin_test --name capsaicin_test --main ../capsaicin
android %test_global_options% create test-project %test_action_options%
