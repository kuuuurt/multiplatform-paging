# Changelog
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.4.4] - 2021-09-16
### What's new
- Fixes Pager on iOS (#19)

## [0.4.3] - 2021-09-12
### What's new
- Library Updates:
     - Gradle to 7.0.2
     - AGP to 7.0.2
     - Kotlin to 1.5.30
     - Coroutines to 1.5.1-native-mt

## [0.4.2] - 2021-06-20
### What's new
- Adds transform operators for `PagingData`

## [0.4.1] - 2021-06-03
### What's new
- Makes `hasNextPage` visible on iOS

## [0.4.0] - 2021-06-03
### What's new
- Library updates
    - Kotlin to 1.5.10
    - paging to 3.0.0
- Removes ktor as a dependency of the library
- Removes paginators and data sources

## [0.3.11] - 2021-04-20
### What's new
- Fixes artifact names

## [0.3.10] - 2021-04-19
### What's new
- Library updates
    - lifecycle-viewmodel-ktx to 2.3.1
    - Kotlin to 1.4.32
    - AGP to 4.1.3
    - paging to 3.0.0-beta03

## [0.3.9] - 2021-02-27
### What's new
- Updates kotlin to 1.4.31, ktor to 1.5.2

## [0.3.8] - 2021-02-13
### What's new
- Updates AGP to 4.1.2
- Updates androidx.paging to 3.0.0-beta01

## [0.3.7] - 2021-02-10
### What's new
- Fixes `Pager` on iOS not loading next items (again)

## [0.3.6] - 2021-02-10
### What's new
- Fixes `Pager` on iOS not loading next items

## [0.3.5] - 2021-02-10
### What's new
- Adds `hasNextPage` to `Pager` in iOS to determine if there are still more pages to be loaded

## [0.3.4] - 2021-02-08
### What's new
- Adds `PagingResult` to configure previous and next keys based on the result when getting the items
- Updates androidx.paging to 3.0.0-alpha13

## [0.3.3] - 2021-02-07
### What's new
- Updates Kotlin to 1.4.30
- Updates ktor to 1.5.1
- Updates coroutines to 1.4.2-native-mt

## [0.3.2] - 2020-12-29
### What's new
- Updates paging to 3.0.0-alpha11
- Updates kotlin to 1.4.21
- Updates coroutines to 1.4.1-native-mt

## [0.3.1] - 2020-10-13
### What's new
- Updates paging to 3.0.0-alpha07
- Updates kotlin to 1.4.10
- Updates ktor to 1.4.1

## [0.3.0] - 2020-09-03
### What's new
- Updates Kotlin to 1.4
- Uses native-mt for coroutines

## [0.2.0] - 2020-06-11
### What's new
- Updates Android's Paging to 3.0.0-alpha01
- Adds Pager, PagingConfig, and PagingData
- Deprecates Paginators, Factories, and Data Sources

## [0.1.3] - 2020-04-23
### What's new
- Fixes bug on refreshing PageKeyedPaginator in iOS

## [0.1.2] - 2020-04-02
### What's new
- Fixes bug on Android PageKeyedDataSource
- Adds parameters to configure pagination size and enabling placeholders on Android

## [0.1.1] - 2020-03-18
### What's new
- Updates Kotlin to 1.3.70

## [0.1.0] - 2020-03-16
### What's new
- Initial Release