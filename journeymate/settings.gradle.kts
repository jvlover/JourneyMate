pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        jcenter()
        maven ("https://devrepo.kakao.com/nexus/content/groups/public/")
        maven ( "https://jitpack.io" )
    }
}

rootProject.name = "JourneyMate"
include(":app")
