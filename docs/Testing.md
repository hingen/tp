---
layout: page
title: Testing guide
---

## Table of Contents

- [Running tests](#running-tests)
- [Types of tests](#types-of-tests)

---

## Running tests

There are two ways to run tests.

- **Method 1: Using IntelliJ JUnit test runner**
  - To run all tests, right-click on the `src/test/java` folder and choose `Run 'All Tests'`
  - To run a subset of tests, you can right-click on a test package,
    test class, or a test and choose `Run 'ABC'`
- **Method 2: Using Gradle**
  - Open a console and run the command `gradlew clean test` (Mac/Linux: `./gradlew clean test`)

:link: **Link**: Read [this Gradle Tutorial from the se-edu/guides](https://se-education.org/guides/tutorials/gradle.html) to learn more about using Gradle.

---

## Types of tests

This project has three types of tests:

1. _Unit tests_ targeting the lowest level methods/classes.\
   e.g. `seedu.address.commons.StringUtilTest`
1. _Integration tests_ that are checking the integration of multiple code units (those code units are assumed to be working).\
   e.g. `seedu.address.storage.StorageManagerTest`
1. Hybrids of unit and integration tests. These test are checking multiple code units as well as how the are connected together.\
   e.g. `seedu.address.logic.LogicManagerTest`
