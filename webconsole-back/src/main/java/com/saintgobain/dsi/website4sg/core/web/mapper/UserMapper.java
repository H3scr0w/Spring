package com.saintgobain.dsi.website4sg.core.web.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import com.saintgobain.dsi.website4sg.core.domain.enumeration.ProjectTypeId;
import com.saintgobain.dsi.website4sg.core.domain.users.UsersEntity;
import com.saintgobain.dsi.website4sg.core.web.bean.LDAPUser;
import com.saintgobain.dsi.website4sg.core.web.bean.UserBody;
import com.saintgobain.dsi.website4sg.core.web.bean.UserBody.UserBodyBuilder;

public class UserMapper {

    public static UserBody toUserBody(UsersEntity userEntity, boolean isAdmin) {
        return toUserBody(userEntity, isAdmin, null, null);
    }

    public static UserBody toUserBody(UsersEntity userEntity, boolean isAdmin, String projectType, String projectCode) {
        if (userEntity == null) {
            return null;
        }

        UserBodyBuilder builder = UserBody.builder()
                .email(userEntity.getEmail())
                .firstname(userEntity.getFirstname())
                .lastname(userEntity.getLastname())
                .isAdmin(userEntity.getIsAdmin())
                .company(userEntity.getCompany())
                .isActive(BooleanUtils.isNotFalse(userEntity.getIsActive()));

        if (isAdmin) {
            builder.accessrightByUsers(userEntity.getAccessrightByUsers() != null ? userEntity.getAccessrightByUsers()
                    .stream().map(accessRightEntity -> AccessRightMapper.toAccessRightBody(accessRightEntity))
                    .collect(Collectors.toList()) : null);
        }

        if (userEntity.getIsAdmin()) {
            builder.role("ADMIN");
        } else if (projectType != null && projectCode != null) {
            String roles = StringUtils.EMPTY;
            if (projectType.equals(ProjectTypeId.D_WEBSITE.getName())) {
                roles = userEntity.getAccessrightByUsers().stream().filter(right -> right.getProject()
                        .getWebsite() != null && StringUtils.equals(right.getProject().getWebsite()
                                .getCode(), projectCode)).map(
                                        right -> right.getRoles().getLabel()).collect(Collectors.joining("|"));

            } else {
                roles = userEntity.getAccessrightByUsers().stream().filter(right -> right.getProject()
                        .getDrupaldocrootcore() != null && StringUtils.equals(right.getProject().getDrupaldocrootcore()
                                .getCode(), projectCode)).map(
                                        right -> right.getRoles().getLabel()).collect(Collectors.joining("|"));
            }

            builder.role(roles);

        }

        return builder.build();
    }

    public static Page<UserBody> toUserBodyPage(Page<UsersEntity> userEntites, boolean isAdmin) {

        List<UserBody> userBodies = userEntites.stream().map(userEntity -> {
            UserBody userBody = toUserBody(userEntity, isAdmin);
            return userBody;
        }).collect(Collectors
                .toList());

        return toUserBodyPage(userEntites, userBodies);
    }

    public static Page<UserBody> toUserBodyPage(Page<UsersEntity> userEntites, boolean isAdmin, String projectType,
            String projectCode) {

        List<UserBody> userBodies = userEntites.stream().map(userEntity -> {
            UserBody userBody = toUserBody(userEntity, isAdmin, projectType, projectCode);
            return userBody;
        }).collect(Collectors
                .toList());

        return toUserBodyPage(userEntites, userBodies);
    }

    public static UsersEntity toUserEntity(UserBody userBody) {
        if (userBody == null) {
            return null;
        }

        return UsersEntity.builder()
                .firstname(userBody.getFirstname())
                .lastname(userBody.getLastname())
                .isAdmin(userBody.getIsAdmin())
                .company(userBody.getCompany())
                .build();
    }

    public static UsersEntity toNewUserEntity(UsersEntity usersEntity, UserBody userBody) {
        if (userBody == null) {
            return null;
        }

        return usersEntity.toBuilder()
                .firstname(userBody.getFirstname())
                .lastname(userBody.getLastname())
                .isAdmin(userBody.getIsAdmin())
                .company(userBody.getCompany())
                .build();
    }

    public static UsersEntity toNewProfile(UsersEntity usersEntity, UserBody userBody) {
        if (userBody == null) {
            return null;
        }

        return usersEntity.toBuilder()
                .firstname(userBody.getFirstname())
                .lastname(userBody.getLastname())
                .company(userBody.getCompany())
                .build();
    }

    public static LDAPUser toLdapUser(UserBody userBody) {
        if (userBody == null) {
            return null;
        }
        return LDAPUser.builder()
                .cn(StringUtils.capitalize(userBody.getFirstname()) + " " + userBody.getLastname().toUpperCase())
                .sn(StringUtils.capitalize(userBody.getFirstname()))
                .uid(userBody.getEmail())
                .wsipcompany(userBody.getCompany())
                .build();
    }

    private static Page<UserBody> toUserBodyPage(Page<UsersEntity> userEntites, List<UserBody> userBodies) {
        Page<UserBody> userBodyPage = new PageImpl<UserBody>(userBodies, userEntites.getPageable(), userEntites
                .getTotalElements());

        return userBodyPage;
    }

}