package demo

import javax.persistence.*


@Entity
@Table(name = "customers")
class CustomerEntity {
    @Id
    var email: String? = null
    var emailDomain: String? = null
    var firstName: String? = null
    var lastName: String? = null
    var password: String? = null
    var day = 0
    var month = 0
    var year = 0
    lateinit var roles: Array<String>

    @ElementCollection
    @CollectionTable(name = "my_friends", joinColumns = [JoinColumn(name = "email")])
    @Column(name = "friends")
    private var friendEmails: MutableSet<String>

    init {
        friendEmails = HashSet()
    }

    fun getFriendEmails(): Set<String> {
        return friendEmails
    }

    fun setFriendEmails(friendEmails: MutableSet<String>) {
        this.friendEmails = friendEmails
    }

    fun addFriend(friendEmail: String) {
        friendEmails.add(friendEmail)
    }
}
