package com.forresthopkinsa.braze

import com.forresthopkinsa.braze.model.*
import com.forresthopkinsa.braze.model.ForgeVersion.F1388
import com.forresthopkinsa.braze.model.ForgeVersion.F1492
import com.forresthopkinsa.braze.model.JavaVersion.J8
import com.forresthopkinsa.braze.spring.Application
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.Assert.assertEquals
import org.testng.Assert.assertTrue
import org.testng.annotations.Test
import java.util.*
import kotlin.reflect.KClass

@SpringBootTest(webEnvironment = RANDOM_PORT, classes = [Application::class])
class BrazeTest : AbstractTestNGSpringContextTests() {

    private val root = "/braze/api"

    @Autowired
    private lateinit var testTemplate: TestRestTemplate

    private val template by lazy {
        testTemplate.withBasicAuth("user", "testpasswordonly")
    }

    val mod1 by lazy {
        val id = rng
        SimpleMod(
                slug = "mod-$id",
                name = "Mod $id",
                author = "Author $id",
                description = "Desc $id!",
                link = null,
                donate = null
        )
    }

    val modVersion1 by lazy {
        IndexedModVersion(
                name = "1.2.$rng",
                index = 0,
                minForge = F1388,
                maxForge = F1492,
                dependencies = listOf(SimpleModVersion("foo", "bar"))
        )
    }

    val pack1 by lazy {
        val id = rng
        SimplePack(
                slug = "pack-$id",
                name = "Pack $id",
                author = "Author $id",
                description = "Desc $id!",
                link = null,
                donate = null
        )
    }

    val packVersion1 by lazy {
        IndexedPackVersion(
                name = "3.4.$rng",
                index = 0,
                forgeVersion = F1492,
                javaVersion = J8,
                memory = null,
                modList = listOf(SimpleModVersion(mod1.slug, modVersion1.name))
        )
    }

    @Test
    fun addMod() {
        val result = post("mods", mod1, Mod::class)
        result.shouldBe(mod1.expand())
    }

    @Test(dependsOnMethods = ["addMod"])
    fun getMods() {
        val result = get("mods", Array<SimpleMod>::class)
        result.body?.list()
        result.shouldBe(arrayOf(mod1))
    }

    @Test(dependsOnMethods = ["addMod"])
    fun addModVersion() {
        val result = post("mods/${mod1.slug}", modVersion1, Mod::class)
        result.shouldBe(mod1.expand().copy(versions = listOf(modVersion1)))
    }

    @Test(dependsOnMethods = ["addModVersion"])
    fun getModVersions() {
        val result = get("mods/${mod1.slug}", Mod::class)
        result.shouldBe(mod1.expand().copy(versions = listOf(modVersion1)))
    }

    @Test
    fun addPack() {
        val result = post("packs", pack1, Pack::class)
        result.shouldBe(pack1.expand())
    }

    @Test(dependsOnMethods = ["addPack"])
    fun getPacks() {
        val result = get("packs", Array<SimplePack>::class)
        result.body?.list()
        result.shouldBe(arrayOf(pack1))
    }

    @Test(dependsOnMethods = ["addPack"])
    fun addPackVersion() {
        val result = post("packs/${pack1.slug}", packVersion1, Pack::class)
        result.shouldBe(pack1.expand().copy(versions = listOf(packVersion1)))
    }

    @Test(dependsOnMethods = ["addPackVersion"])
    fun getPackVersions() {
        val result = get("packs/${pack1.slug}", Pack::class)
        result.shouldBe(pack1.expand().copy(versions = listOf(packVersion1)))
    }

    private fun <T : Any> get(path: String, responseType: KClass<T>) =
            template.getForEntity("$root/$path", responseType.java)

    private fun <T : Any> post(path: String, body: Any, responseType: KClass<T>) =
            template.postForEntity("$root/$path", HttpEntity(body), responseType.java)

    private fun <T> ResponseEntity<T>.shouldBeOk() {
        assertEquals(statusCode, HttpStatus.OK)
        println("\nResult: ${body.toString()}\n")
    }

    private fun <T> ResponseEntity<T>.shouldBe(expected: T) {
        shouldBeOk()
        assertEquals(body, expected)
    }

    private fun <T> ResponseEntity<Array<T>>.shouldContain(expected: T) {
        shouldBeOk()
        assertTrue(body!!.contains(expected))
    }

    private fun <T> Array<T>.list() {
        forEach { println(it) }
    }

    private val rng
        get() = Random().nextInt(900) + 100

}