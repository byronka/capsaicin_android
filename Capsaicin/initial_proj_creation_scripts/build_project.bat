REM -n --name          : Project name.
REM -v --gradle-version: Gradle Android plugin version.
REM -t --target        : Target ID of the new project. [required]
REM -p --path          : The new project's directory. [required]
REM -g --gradle        : Use gradle template.
REM -k --package       : Android package name for the application. [required]
REM -a --activity      : Name of the default Activity that is created. [required]

set global_options=--verbose
set action_options=--name capsaicin --target 9 --path capsaicin --package com.renomad.capsaicin --activity LoginActivity
android %global_options% create project %action_options%
