package com.andersen.manageclients.repository.specification

import com.andersen.manageclients.model.Client
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Component

@Component
class ClientSpecification {
    fun firstNameAndEmailAndLastNameLike(firstName: String?, lastName: String?): Specification<Client> {
        val firstNameSpecification = firstNameLike(firstName)
        val lastNameSpecification = lastNameLike(lastName)
        //fix cases when "" (empty string)

        if (firstNameSpecification != null) {
            
        }
        return Specification<Client> { root, query, criteriaBuilder ->
            criteriaBuilder.and(
                firstNameSpecification.toPredicate(root, query, criteriaBuilder),
                lastNameSpecification.toPredicate(root, query, criteriaBuilder))
        }
    }

    fun firstNameLike(firstName: String?): Specification<Client> {
        return Specification<Client> { root, _, criteriaBuilder ->
            if (!firstName.isNullOrBlank()) {
                val pattern = "%$firstName%"
                val firstNamePredicate = criteriaBuilder.like(root["firstName"], pattern)
                criteriaBuilder.or(firstNamePredicate)
            } else {
                null
            }
        }
    }

    fun lastNameLike(lastName: String?): Specification<Client> {
        return Specification<Client> { root, _, criteriaBuilder ->
            if (!lastName.isNullOrBlank()) {
                val pattern = "%$lastName%"
                val lastNamePredicate = criteriaBuilder.like(root["lastName"], pattern)
                criteriaBuilder.or(lastNamePredicate)
            } else {
                null
            }

        }
    }
}