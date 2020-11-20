package com.wg_planner.backend.entity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.*;
import java.util.function.Function;

@Entity
@Table(name = "account")
@Inheritance(strategy = InheritanceType.JOINED)
//account is of type UserDetails?
public class Account extends AbstractEntity implements UserDetails, CredentialsContainer, Cloneable {
    private static final Log logger = LogFactory.getLog(Account.class);

    @NotBlank
    @Size(max = 255)
    private String firstName;

    @NotBlank
    @Size(max = 255)
    private String lastName;

    @Email
    @Size(max = 255)
    @Column(unique = true)
    private String email;

    @NotBlank
    @Size(max = 255)
    private String username;

    @NotNull
    @Size(min = 6, max = 255)
    private String passwordHash;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<GrantedAuthority> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;


    public Account() {
    }

    public Account(String firstName, String lastName, String email, String username, String passwordHash, Collection<? extends GrantedAuthority> authorities) {
        this(firstName, lastName, email, username, passwordHash, true, true, true, true, authorities);
    }

    public Account(String firstName, String lastName, String email, String username, String passwordHash, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        if (username != null && !"".equals(username) && passwordHash != null) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.username = username;
            this.passwordHash = passwordHash;
            this.enabled = enabled;
            this.accountNonExpired = accountNonExpired;
            this.credentialsNonExpired = credentialsNonExpired;
            this.accountNonLocked = accountNonLocked;
            this.authorities = Collections.unmodifiableSet(sortAuthorities(authorities));
        } else {
            throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @PrePersist
    @PreUpdate
    private void prepareData() {
        this.email = email == null ? null : email.toLowerCase();
    }

    public Collection<GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public String getPassword() {
        return this.passwordHash;
    }

    public String getUsername() {
        return this.username;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    public void eraseCredentials() {
        this.passwordHash = null;
    }

    private static SortedSet<GrantedAuthority> sortAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Cannot pass a null GrantedAuthority collection");
        SortedSet<GrantedAuthority> sortedAuthorities = new TreeSet(new Account.AuthorityComparator());
        Iterator var2 = authorities.iterator();

        while (var2.hasNext()) {
            GrantedAuthority grantedAuthority = (GrantedAuthority) var2.next();
            Assert.notNull(grantedAuthority, "GrantedAuthority list cannot contain any null elements");
            sortedAuthorities.add(grantedAuthority);
        }

        return sortedAuthorities;
    }

    public boolean equals(Object rhs) {
        return rhs instanceof Account ? this.username.equals(((Account) rhs).username) : false;
    }

    public int hashCode() {
        return this.username.hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append(": ");
        sb.append("Username: ").append(this.username).append("; ");
        sb.append("Password: [PROTECTED]; ");
        sb.append("Enabled: ").append(this.enabled).append("; ");
        sb.append("AccountNonExpired: ").append(this.accountNonExpired).append("; ");
        sb.append("credentialsNonExpired: ").append(this.credentialsNonExpired).append("; ");
        sb.append("AccountNonLocked: ").append(this.accountNonLocked).append("; ");
//        sb.append(super.toString());
        if (!this.authorities.isEmpty()) {
            sb.append("Granted Authorities: ");
            boolean first = true;
            Iterator var3 = this.authorities.iterator();

            while (var3.hasNext()) {
                GrantedAuthority auth = (GrantedAuthority) var3.next();
                if (!first) {
                    sb.append(",");
                }

                first = false;
                sb.append(auth);
            }
        } else {
            sb.append("Not granted any authorities");
        }

        return sb.toString();
    }

    public static Account.AccountBuilder withUsername(String username) {
        return builder().username(username);
    }

    public static Account.AccountBuilder builder() {
        return new Account.AccountBuilder();
    }

    /**
     * @deprecated
     */
    @Deprecated
    public static Account.AccountBuilder withDefaultPasswordEncoder() {
        logger.warn("User.withDefaultPasswordEncoder() is considered unsafe for production and is only intended for sample applications.");
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        Account.AccountBuilder var10000 = builder();
        encoder.getClass();
        return var10000.passwordEncoder(encoder::encode);
    }

    public static Account.AccountBuilder withUserDetails(UserDetails userDetails) {
        return withUsername(userDetails.getUsername()).password(userDetails.getPassword()).accountExpired(!userDetails.isAccountNonExpired()).accountLocked(!userDetails.isAccountNonLocked()).authorities(userDetails.getAuthorities()).credentialsExpired(!userDetails.isCredentialsNonExpired()).disabled(!userDetails.isEnabled());
    }

    public Long getId() {
        return super.getId();
    }

    public static class AccountBuilder {
        private String firstName;
        private String lastName;
        private String email;
        private String username;
        private String password;
        private List<GrantedAuthority> authorities;
        private boolean accountExpired;
        private boolean accountLocked;
        private boolean credentialsExpired;
        private boolean disabled;
        private Function<String, String> passwordEncoder;
        private static final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        private AccountBuilder() {
            this.passwordEncoder = (password) -> {
                return password;
            };
        }

        public Account.AccountBuilder firstName(String firstName) {
            Assert.notNull(firstName, "firstName cannot be null");
            this.firstName = firstName;
            return this;
        }

        public Account.AccountBuilder lastName(String lastName) {
            Assert.notNull(lastName, "lastName cannot be null");
            this.lastName = lastName;
            return this;
        }

        public Account.AccountBuilder email(String email) {
            boolean emailValid;
            Assert.notNull(email, "email cannot be null");
            if (!email.matches(EMAIL_PATTERN)) {
                emailValid = false;
            } else {
                emailValid = true;
            }
            Assert.isTrue(emailValid, "invalid email address format");
            this.email = email;
            return this;
        }

        public Account.AccountBuilder username(String username) {
            Assert.notNull(username, "username cannot be null");
            this.username = username;
            return this;
        }

        public Account.AccountBuilder password(String password) {
            Assert.notNull(password, "password cannot be null");
            this.password = password;
            return this;
        }

        public Account.AccountBuilder passwordEncoder(Function<String, String> encoder) {
            Assert.notNull(encoder, "encoder cannot be null");
            this.passwordEncoder = encoder;
            return this;
        }

        public Account.AccountBuilder roles(String... roles) {
            List<GrantedAuthority> authorities = new ArrayList(roles.length);
            String[] var3 = roles;
            int var4 = roles.length;

            for (int var5 = 0; var5 < var4; ++var5) {
                String role = var3[var5];
                Assert.isTrue(!role.startsWith("ROLE_"), () -> {
                    return role + " cannot start with ROLE_ (it is automatically added)";
                });
                authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }

            return this.authorities((Collection) authorities);
        }

        public Account.AccountBuilder authorities(GrantedAuthority... authorities) {
            return this.authorities((Collection) Arrays.asList(authorities));
        }

        public Account.AccountBuilder authorities(Collection<? extends GrantedAuthority> authorities) {
            this.authorities = new ArrayList(authorities);
            return this;
        }

        public Account.AccountBuilder authorities(String... authorities) {
            return this.authorities((Collection) AuthorityUtils.createAuthorityList(authorities));
        }

        public Account.AccountBuilder accountExpired(boolean accountExpired) {
            this.accountExpired = accountExpired;
            return this;
        }

        public Account.AccountBuilder accountLocked(boolean accountLocked) {
            this.accountLocked = accountLocked;
            return this;
        }

        public Account.AccountBuilder credentialsExpired(boolean credentialsExpired) {
            this.credentialsExpired = credentialsExpired;
            return this;
        }

        public Account.AccountBuilder disabled(boolean disabled) {
            this.disabled = disabled;
            return this;
        }

        public UserDetails build() {
            String encodedPassword = (String) this.passwordEncoder.apply(this.password);
            return new Account(this.firstName, this.lastName, this.email, this.username, encodedPassword, !this.disabled, !this.accountExpired, !this.credentialsExpired, !this.accountLocked, this.authorities);
        }
    }

    private static class AuthorityComparator implements Comparator<GrantedAuthority>, Serializable {
        private static final long serialVersionUID = 530L;

        private AuthorityComparator() {
        }

        public int compare(GrantedAuthority g1, GrantedAuthority g2) {
            if (g2.getAuthority() == null) {
                return -1;
            } else {
                return g1.getAuthority() == null ? 1 : g1.getAuthority().compareTo(g2.getAuthority());
            }
        }
    }
}
