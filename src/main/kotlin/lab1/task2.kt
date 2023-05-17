package lab1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


class BinaryTree<K : Comparable<K>, V> {
    private data class Node<K, V>(var key: K, var value: V, var left: Node<K, V>? = null, var right: Node<K, V>? = null)

    private var root: Node<K, V>? = null

    fun add(key: K, value: V) {
        root = addNode(root, key, value)
    }

    private fun addNode(node: Node<K, V>?, key: K, value: V): Node<K, V> {
        node ?: return Node(key, value)

        when {
            key < node.key -> node.left = addNode(node.left, key, value)
            key > node.key -> node.right = addNode(node.right, key, value)
            else -> node.value = value
        }

        return node
    }

    fun remove(key: K) {
        root = removeNode(root, key)
    }

    private fun removeNode(node: Node<K, V>?, key: K): Node<K, V>? {
        node ?: return null

        when {
            key < node.key -> node.left = removeNode(node.left, key)
            key > node.key -> node.right = removeNode(node.right, key)
            else -> {
                when {
                    node.left == null && node.right == null -> return null
                    node.left == null -> return node.right
                    node.right == null -> return node.left
                    else -> {
                        val successor = findSuccessor(node.right!!)
                        node.key = successor.key
                        node.value = successor.value
                        node.right = removeNode(node.right, successor.key)
                    }
                }
            }
        }

        return node
    }

    private fun findSuccessor(node: Node<K, V>): Node<K, V> {
        var current = node
        while (current.left != null) {
            current = current.left!!
        }
        return current
    }

    fun find(key: K): V? {
        var current = root
        while (current != null) {
            when {
                key < current.key -> current = current.left
                key > current.key -> current = current.right
                else -> return current.value
            }
        }
        return null
    }

    fun traverseInOrder(action: (K, V) -> Unit) {
        traverseInOrderRecursive(root, action)
    }

    private fun traverseInOrderRecursive(node: Node<K, V>?, action: (K, V) -> Unit) {
        node ?: return
        traverseInOrderRecursive(node.left, action)
        action(node.key, node.value)
        traverseInOrderRecursive(node.right, action)
    }
}


class BinaryTreeTest {
    @Test
    fun `add and find`() {
        val tree = BinaryTree<Int, String>()

        tree.add(4, "four")
        tree.add(2, "two")
        tree.add(1, "one")
        tree.add(3, "three")
        tree.add(6, "six")
        tree.add(5, "five")
        tree.add(7, "seven")

        assertEquals("four", tree.find(4))
        assertEquals("one", tree.find(1))
        assertEquals("seven", tree.find(7))
        assertNull(tree.find(0))
        assertNull(tree.find(8))
    }

    @Test
    fun `remove`() {
        val tree = BinaryTree<Int, String>()

        tree.add(4, "four")
        tree.add(2, "two")
        tree.add(1, "one")
        tree.add(3, "three")
        tree.add(6, "six")
        tree.add(5, "five")
        tree.add(7, "seven")

        tree.remove(4)
        assertNull(tree.find(4))
        assertEquals("one", tree.find(1))
        assertEquals("seven", tree.find(7))

        tree.remove(1)
        assertNull(tree.find(1))
        assertEquals("two", tree.find(2))
        assertEquals("three", tree.find(3))

        tree.remove(7)
        assertNull(tree.find(7))
        assertEquals("six", tree.find(6))
        assertEquals("five", tree.find(5))
    }

    @Test
    fun `traverseInOrder`() {
        val tree = BinaryTree<Int, String>()

        tree.add(4, "four")
        tree.add(2, "two")
        tree.add(1, "one")
        tree.add(3, "three")
        tree.add(6, "six")
        tree.add(5, "five")
        tree.add(7, "seven")

        val expected = listOf(1 to "one", 2 to "two", 3 to "three", 4 to "four", 5 to "five", 6 to "six", 7 to "seven")
        val actual = mutableListOf<Pair<Int, String>>()
        tree.traverseInOrder { key, value ->
            actual.add(key to value)
        }
        assertEquals(expected, actual)
    }
}

fun main() {
    val tree = BinaryTree<Int, String>()

    tree.add(4, "four")
    tree.add(2, "two")
    tree.add(1, "one")
    tree.add(3, "three")
    tree.add(6, "six")
    tree.add(5, "five")
    tree.add(7, "seven")

    println("In-order traversal:")
    tree.traverseInOrder { key, value ->
        println("$key: $value")
    }

    val keyToRemove = 4
    tree.remove(keyToRemove)
    println("After removing key $keyToRemove:")
    tree.traverseInOrder { key, value ->
        println("$key: $value")
    }

    val keyToFind = 6
    val value = tree.find(keyToFind)
    if (value != null) {
        println("Value of key $keyToFind: $value")
    } else {
        println("Key $keyToFind not found")
    }

    val newKey = 8
    val newValue = "eight"
    tree.add(newKey, newValue)
    println("After adding key $newKey:")
    tree.traverseInOrder { key, value ->
        println("$key: $value")
    }
}

