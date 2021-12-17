# WiZNPhotos


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
