# Multiplatform Paging

[ ![Download](https://maven-badges.herokuapp.com/maven-central/io.github.kuuuurt/multiplatform-paging/badge.svg) ](https://search.maven.org/search?q=a:multiplatform-paging)

A Kotlin Multiplatform library for pagination.

## :warning: **Important Notice**

This repository is no longer actively maintained.

If you're looking for more features or alternative solutions, check out [Cash App's Multiplatform Paging library](https://github.com/cashapp/multiplatform-paging)

## Setup

This library is used on Kotlin Multiplatform that targets Android and iOS.

Check the table below for the compatibility across versions

| Library | Kotlin | Paging        |
|---------|--------| ------------- |
| 0.6.2   | 1.8.0  | 3.1.1         |
| 0.6.1   | 1.8.0  | 3.1.1         |
| 0.6.0   | 1.7.20 | 3.1.1         |
| 0.5.0   | 1.7.10 | 3.1.1         |
| 0.4.7   | 1.6.10 | 3.1.0         |
| 0.4.6   | 1.6.0  | 3.1.0         |
| 0.4.5   | 1.5.31 | 3.0.1         |
| 0.4.4   | 1.5.30 | 3.0.1         |
| 0.4.3   | 1.5.30 | 3.0.1         |
| 0.4.2   | 1.5.10 | 3.0.0         |
| 0.4.1   | 1.5.10 | 3.0.0         |
| 0.4.0   | 1.5.10 | 3.0.0         |
| 0.3.11  | 1.4.32 | 3.0.0-beta03  |
| 0.3.10  | 1.4.32 | 3.0.0-beta03  |
| 0.3.9   | 1.4.31 | 3.0.0-beta01  |
| 0.3.8   | 1.4.30 | 3.0.0-beta01  |
| 0.3.4+  | 1.4.30 | 3.0.0-alpha13 |
| 0.3.3   | 1.4.30 | 3.0.0-alpha11 |
| 0.3.2   | 1.4.21 | 3.0.0-alpha11 |
| 0.3.1   | 1.4.10 | 3.0.0-alpha07 |
| 0.3.0   | 1.4.0  | 3.0.0-alpha06 |
| 0.2.0   | 1.3.70 | 3.0.0-alpha01 |
| 0.1.+   | 1.3.70 | 2.1.1         |
| 0.1.0   | 1.3.61 | 2.1.1         |

Add the `mavenCentral` repository on your Project-level gradle
```kotlin
allprojects {
    repositories {
        ...
        mavenCentral()
    }
}
```

On the module, add the library in your dependencies.

```kotlin
kotlin {
    ...
    sourceSets["commonMain"].dependencies {
        implementation("io.github.kuuuurt:multiplatform-paging:{version}")
    }
}
```

On Android, make sure to add `androidx.paging:paging-runtime` as a dependency

On iOS, you have to export it on your targets
```kotlin
kotlin {
    ...
    cocoapods {
        ...
        framework {
            ...
            export("io.github.kuuuurt:multiplatform-paging:{version}")
        }
    }

    val commonMain by sourceSets.getting {
        dependencies {
            api("io.github.kuuuurt:multiplatform-paging:{version}")
            ...
        }
    }
}
```

## Usage

### Common

Multiplatform paging exposes paginators which you can use in your multiplatform code to have common pagination on Android and iOS.

```kotlin
class MyMultiplatformController {
    private val pager = Pager<Int, String>(
        clientScope = coroutineScope,
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false // Ignored on iOS
        ),
        initialKey = 1, // Key to use when initialized
        getItems = { currentKey, size ->
            val items = ... // How you will get the items (API Call or Local DB)
            PagingResult(
                items = items,
                currentKey = currentKey,
                prevKey = { _, _ -> null }, // Key for previous page, null means don't load previous pages
                nextKey = { items, currentKey -> currentKey + 1 } // Key for next page. Use `items` or `currentKey` to get it depending on the pagination strategy
            )
        }
    )

    val pagingData: CommonFlow<PagingData<String>>
        get() = pager.pagingData
            .cachedIn(clientScope) // cachedIn from AndroidX Paging. on iOS, this is a no-op
            .asCommonFlow() // So that iOS can consume the Flow 
}
```

*`CommonFlow` is a helper we can use to consume `Flow` on iOS. See [`FlowHelpers`](https://github.com/kuuuurt/multiplatform-paging/blob/develop/sample/multiplatform-library/src/commonMain/kotlin/com/kuuurt/paging/sample/multiplatform/library/helpers/FlowHelpers.kt)*

### Android

On Android, multiplatform paging uses [Android Architecture Component's Paging library](https://developer.android.com/topic/libraries/architecture/paging) and exposes `pagedList` as a `Flow<androidx.paging.PagedList<T>>` which can be observed and submitted onto the `PagedListAdapter`.

```kotlin
class MyFragment : Fragment() {
    val myMultiplatformController = MyMultiplatformController()
    val myPagingDataAdapter = MyPagingDataAdapter()
    
    override fun onViewCreated(...) {
        super.onViewCreated(...)
      
        myMultiplatformController.pagingData
            .onEach { myPagingDataAdapter.submitData(it) }
            .launchIn(viewLifecyleOwner.lifecyclerScope)     
    }
}
```

### iOS

On iOS, it exposes `pagedList` as a `Flow<List<T>>` which can be observed and rendered to a `UITableView`

```swift
class MyViewController UIViewController, UITableViewDelegate, UITableViewDataSource {
    @IBOutlet weak var tableView: UITableView!
    
    let myMultiplatformController = MyMultiplatformController()
    
    private var data: [T] = []
    private var count: Int = 0
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // setup...
        
        myMultiplatformController.pagingData.watch { [unowned self] nullableArray in
            guard let list = nullableArray?.compactMap({ $0 as? T }) else {
                return
            }
      
            self.data = list
            self.count = list.count
            self.tableView.reloadData()
        }
    }
}
```

*Disclaimer: I'm not an iOS developer and this is what I was able to make of. If someone has a better example, contributions are welcome!*

### Jetpack Compose and SwiftUI

For samples using Jetpack Compose and SwiftUI, you can refer to [MortyComposeKMM](https://github.com/joreilly/MortyComposeKMM) by [joreilly](https://github.com/joreilly).

## Maintainers

- Kurt Renzo Acosta - [kurt.r.acosta@gmail.com](mailto:kurt.r.acosta@gmail.com)

## Contributing

Feel free to dive in! [Open an issue](https://github.com/kuuuurt/multiplatform-paging/issues/new) or submit PRs.

## License

[Apache-2.0](LICENSE) © Kurt Renzo Acosta
