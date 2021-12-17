# WiZNPhotos

[![tests](https://github.com/siddhantkumarupmanyu/WiZNPhotos/actions/workflows/tests.yml/badge.svg)](https://github.com/siddhantkumarupmanyu/WiZNPhotos/actions/workflows/tests.yml)

https://user-images.githubusercontent.com/66074842/146529321-76d15231-0d72-4584-b4dc-bdc6b4cb28f3.mp4


## Features
- Uses Coroutines, following Structured Concurrency Principles
- Uses Flow, instead of relying on LiveData Android dependency
- Good Coverage with End To End, Integration and Unit Tests
- Material.io guide/components for Ui
- Github Actions CI
- Progress Bar when loading
- Automatically Refresh when local data is deleted.

## Architecture
- MVVM
- Test Driven Development (TDD)
- Emergent/Evolutionary/Incremental Design
- SOLID, DRY, KISS and YAGNI(now)

## Dependency Injection
- Hilt

## Libraries
- Android Jetpack Libraries
  * Room
  * Navigation
  * DataBinding
- Kotlin
  * Flow
  * Coroutine
- Third Party
  * Retrofit
  * GSON
  * Glide
## Tests
- mockito
- Junit
- Espresso
- kotlinx-coroutines-test
- mockwebserver

## Improvements/Things I want to add
- using ViewPager2 in ItemFragment with pagination
  * this will give a sliding functionality
- implement pagination on ListFragment
  * loading 5000 items at once in memory is not optimized

### More Info
see [scratchpad.txt](scratchpad.txt)
