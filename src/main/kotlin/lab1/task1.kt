package lab1

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

data class Contact(val name: String, val phoneNumber: String)
class MobilePhone(private val myNumber: String) {
    private val myContacts = ArrayList<Contact>()

    fun addNewContact(newContact: Contact): Boolean {
        if (findContact(newContact) >= 0) {
            println("Контакт уже существует")
            return false
        }
        myContacts.add(newContact)
        return true
    }

    fun updateContact(oldContact: Contact, newContact: Contact): Boolean {
        val foundPosition = findContact(oldContact)
        if (foundPosition < 0) {
            println("Контакт ${oldContact.name} не найден")
            return false
        } else if (findContact(newContact) != -1) {
            println("Контакт с именем ${newContact.name} уже существует. Обновление не выполнено.")
            return false
        }
        myContacts[foundPosition] = newContact
        println("Контакт ${oldContact.name} заменен на ${newContact.name}.")
        return true
    }

    fun removeContact(contact: Contact): Boolean {
        val foundPosition = findContact(contact)
        if (foundPosition < 0) {
            println("Контакт ${contact.name} не найден")
            return false
        }
        myContacts.removeAt(foundPosition)
        println("Контакт ${contact.name} удален.")
        return true
    }

    private fun findContact(contact: Contact): Int {
        return myContacts.indexOf(contact)
    }

    fun queryContact(name: String): Contact? {
        for (i in myContacts.indices) {
            val contact = myContacts[i]
            if (contact.name == name) {
                return contact
            }
        }
        return null
    }

    fun printContacts() {
        println("Список контактов")
        for (i in myContacts.indices) {
            println("${i + 1}. ${myContacts[i].name} -> ${myContacts[i].phoneNumber}")
        }
    }
}

class MobilePhoneTest {
    private val mobilePhone = MobilePhone("1234567890")

    @Test
    fun testAddNewContact() {
        val contact1 = Contact("Иван", "8477663434")
        assertTrue(mobilePhone.addNewContact(contact1))

        val contact2 = Contact("Мария", "8342343444")
        assertTrue(mobilePhone.addNewContact(contact2))

        assertFalse(mobilePhone.addNewContact(contact1))
    }

    @Test
    fun testUpdateContact() {
        val contact1 = Contact("Иван", "8477663434")
        mobilePhone.addNewContact(contact1)

        val contact2 = Contact("Мария", "8342343444")
        mobilePhone.addNewContact(contact2)

        val updatedContact = Contact("Алексей", "8948433333")
        assertTrue(mobilePhone.updateContact(contact1, updatedContact))

        assertFalse(mobilePhone.updateContact(Contact("Петр", "8493227282"), updatedContact))
        assertFalse(mobilePhone.updateContact(contact2, updatedContact))
    }

    @Test
    fun testRemoveContact() {
        val contact1 = Contact("Иван", "8477663434")
        mobilePhone.addNewContact(
            contact1
        )
        val contact2 = Contact("Мария", "8342343444")
        mobilePhone.addNewContact(contact2)

        assertTrue(mobilePhone.removeContact(contact1))
        assertFalse(mobilePhone.removeContact(Contact("Петр", "8493227282")))
        assertTrue(mobilePhone.removeContact(contact2))
    }

    @Test
    fun testQueryContact() {
        val contact1 = Contact("Иван", "8477663434")
        mobilePhone.addNewContact(contact1)

        val contact2 = Contact("Мария", "8342343444")
        mobilePhone.addNewContact(contact2)

        assertEquals(contact1, mobilePhone.queryContact("Иван"))
        assertEquals(contact2, mobilePhone.queryContact("Мария"))
        assertNull(mobilePhone.queryContact("Петр"))
    }
}

fun main() {
    val mobilePhone = MobilePhone("1234567890")
    mobilePhone.addNewContact(Contact("Иван", "8477663434"))
    mobilePhone.addNewContact(Contact("Мария", "8342343444"))
    mobilePhone.addNewContact(Contact("Петр", "1234567890"))

    mobilePhone.printContacts()

    println("----------------------------------------------------------------------------------")

    mobilePhone.updateContact(Contact("Мария", "8342343444"), Contact("Мария Иванова", "8845255355"))
    mobilePhone.printContacts()
    println("----------------------------------------------------------------------------------")

    mobilePhone.removeContact(Contact("Петр", "1234567890"))
    mobilePhone.printContacts()
    println("----------------------------------------------------------------------------------")

    println(mobilePhone.queryContact("Алексей Кузнецов"))
    println(mobilePhone.queryContact("Петр"))
}