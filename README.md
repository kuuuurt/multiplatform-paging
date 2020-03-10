# Multiplatform Paging

A Kotlin Multiplatform library for pagination.

## Setup

This library is used on Kotlin Multiplatform that targets Android and iOS. Make sure you have the following setup for your multiplatform library

```kotlin
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.multiplatform")
    id("org.jetbrains.kotlin.native.cocoapods")
}

android {
    // Android configurations
}

version = "1.0.0"

kotlin {
    cocoapods {
        summary = "Shared module for Android and iOS"
        homepage = "Link to a Kotlin/Native module homepage"
    }

    ios {
        compilations {
            val main by getting {
                kotlinOptions.freeCompilerArgs = listOf("-Xobjc-generics")
            }
        }
    }
    android()

    sourceSets["commonMain"].dependencies {
        // Common dependencies
    }

    sourceSets["iosMain"].dependencies {
        // iOS dependencies
    }
}

dependencies {
    // Android dependencies
}

```

Then use Multiplatform Paging on Kotlin Multiplatform code for your Android and iOS targets.

## Usage

### Common

Multiplatform paging exposes paginators which you can use in your multiplatform code to have common pagination on Android and iOS.

```kotlin
class MyMultiplatformController {
    val positionalPaginator = PositionalPaginator(
        clientScope = coroutineScope,
        getCount = { ... },
        getItems = { startAt, size -> ... }
    )
    
    val pageKeyedPaginator = PageKeyedPaginator(
        clientScope = coroutineScope,
        getCount = { ... },
        getItems = { page, size -> ... }
    )
}
```

The library uses experimental APIs so you have to use `@UseExperimental` annotations.

### Android

On Android, multiplatform paging uses [Android Architecture Component's Paging library](https://developer.android.com/topic/libraries/architecture/paging) and exposes `pagedList` as a `Flow<androidx.paging.PagedList<T>>` which can be observed and submitted onto the `PagedListAdapter`.

```kotlin
class MyFragment : Fragment() {
    val myMultiplatformController = MyMultiplatformController()
    val myPagedListAdapter = MyPagedListAdapter()
    
    override fun onViewCreated(...) {
        super.onViewCreated(...)
        
        myMultiplatformController.positionalPaginator.pagedList
            .onEach { myPagedListAdapter.submitList(it) }
            .launchIn(viewLifecyleOwner.lifecyclerScope)
            
        // Or if you're using LiveData KTX
        myMultiplatformController.positionalPaginator.pagedList.asLiveData().observe(viewLifecycleOwner) {
            myPagedListAdapter.submitList(it)
        }
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
        
        myMultiplatformController.paginator.pagedList.watch { [unowned self] nullableArray in
            guard let list = nullableArray?.compactMap({ $0 as? T }) else {
                return
            }
      
            self.data = list
            self.tableView.reloadData()
        }
    
        myMultiplatformController.paginator.totalCount.watch { [unowned self] nullable in
            guard let totalCount = nullable as? Int else {
                return
            }
            self.count = totalCount
            self.tableView.reloadData()
        }
    }
}
```

*Disclaimer: I'm not an iOS developer and this is what I was able to make of. If someone has a better example, contributions are welcome!*

## Install

Add this in your gradle scripts

Make sure you're using the jcenter repository on your Project-level gradle
```kotlin
allprojects {
    repositories {
        ...
        jcenter()
    }
}
```

On the module-level, add the library as an `api` dependency so it can get propagated to the consumers.
```kotlin
kotlin {
    ...
    sourceSets["commonMain"].dependencies {
        api("com.kuuuurt:multiplatform-paging:0.1.1")
    }
}
```

## Maintainers

- Kurt Renzo Acosta - [kurt.r.acosta@gmail.com](mailto:kurt.r.acosta@gmail.com)

## Contributing

Feel free to dive in! [Open an issue](https://github.com/kuuuurt/multiplatform-paging/issues/new) or submit PRs.

## License

[Apache-2.0](LICENSE) © Kurt Renzo Acosta
