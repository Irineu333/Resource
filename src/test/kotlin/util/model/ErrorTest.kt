package util.model

data class ErrorTest(
    override val message: String? = null
) : Throwable(message) {

    override fun equals(other: Any?): Boolean {
        return if (other is ErrorTest) {
            message == other.message
        } else false
    }

    override fun hashCode(): Int {
        return message?.hashCode() ?: 0
    }
}