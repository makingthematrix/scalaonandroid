#!/bin/sh

mvn -X -e -Dandroid.release=true -Pandroid clean gluonfx:build gluonfx:package