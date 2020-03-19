# Multiplatform Paging

[ ![Download](https://api.bintray.com/packages/kuuuurt/libraries/multiplatform-paging/images/download.svg?version=0.1.0) ](https://bintray.com/kuuuurt/libraries/multiplatform-paging/0.1.0/link)

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

The library uses experimental APIs so you have to use `@OptIn` annotations.

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

        myMultiplatformController.positionalPaginator.getState
            .onEach {
                when(it) {
                    PaginatorState.Complete -> ...
                    PaginatorState.Loading -> ...
                    PaginatorState.Empty -> ...
                    is PaginatorState.Error -> ...
                }
            }
            .launchIn(viewLifecyleOwner.lifecyclerScope)
            
        // Or if you're using LiveData KTX
        myMultiplatformController.positionalPaginator.pagedList.asLiveData().observe(viewLifecycleOwner) {
            myPagedListAdapter.submitList(it)
        }

        myMultiplatformController.positionalPaginator.getState.asLiveData().observe(viewLifecycleOwner) {
            when(it) {
                PaginatorState.Complete -> ...
                PaginatorState.Loading -> ...
                PaginatorState.Empty -> ...
                is PaginatorState.Error -> ...
            }
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

        myMultiplatformController.paginator.getState.watch { [unowned self] nullable in
            guard let state = nullable else {
                return
            }

            switch(state) {
            case is PaginatorState.Complete: break
            case is PaginatorState.Loading: break
            case is PaginatorState.Empty: break
            case let errorState as PaginatorState.Error: break
            default: break
            }
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

## Installation

Check the table below for the compatibilty across Kotlin versions

| Library    | Kotlin  |
| ---------- | ------- |
| 0.1.1      | 1.3.70  |
| 0.1.0      | 1.3.61  |

Add the jcenter repository on your Project-level gradle
```kotlin
allprojects {
    repositories {
        ...
        jcenter()
    }
}
```

On the module-level, add the library as an `api` dependency. The library needs to be propagated to the platforms.

On Android, it's automatically handled by Gradle.
On iOS, you have to export it on your targets
```kotlin
kotlin {
    ...

    targets.named<KotlinNativeTarget>("iosX64") {
        binaries.withType<Framework>().configureEach {
            export("com.kuuuurt:multiplatform-paging-iosX64:0.1.1")
        }
    }

    targets.named<KotlinNativeTarget>("iosArm64") {
        binaries.withType<Framework>().configureEach {
            export("com.kuuuurt:multiplatform-paging-iosArm64:0.1.1")
        }
    }

    sourceSets["commonMain"].dependencies {
        api("com.kuuuurt:multiplatform-paging:0.1.1")
    }
}
```

This uses Gradle Module Metadata so you don't have to put the dependencies on each target. To take advantage of this, enable it in your `settings.gradle` file

```kotlin
enableFeaturePreview("GRADLE_METADATA")
```

## Maintainers

- Kurt Renzo Acosta - [kurt.r.acosta@gmail.com](mailto:kurt.r.acosta@gmail.com)

## Contributing

Feel free to dive in! [Open an issue](https://github.com/kuuuurt/multiplatform-paging/issues/new) or submit PRs.

## License

[Apache-2.0](LICENSE) © Kurt Renzo Acosta
