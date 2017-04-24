#!/usr/bin/env bash
pushd madcow-project/build/distributions
unzip -o madcow-2.0-SNAPSHOT-install.zip -d madcow-2.0-SNAPSHOT-install
pushd madcow-2.0-SNAPSHOT-install
#for dir in ../../../src/acceptance-test
#do
#    dir=${dir%*/}
#    #echo "${dir#*}"
#    sh runMadcow.sh -s ../${dir#*/}/tests
#done
sh runMadcow.sh -s ../../../src/acceptance-test/tests
#popd
#popd

