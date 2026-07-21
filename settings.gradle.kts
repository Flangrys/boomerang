rootProject.name = "boomerang"

include(":boomerang-core")
include(":boomerang-proto")
include(":boomerang-plugin")

pluginManagement {
    includeBuild("boomerang-build")
}