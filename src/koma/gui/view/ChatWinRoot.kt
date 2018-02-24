package view

import controller.ChatController
import controller.guiEvents
import koma.controller.requests.membership.ask_invite_member
import koma.controller.requests.membership.runAskBanRoomMember
import koma.controller.requests.room.createRoomInteractive
import koma.gui.view.window.preferences.PreferenceWindow
import koma.gui.view.window.roomfinder.RoomFinder
import koma.storage.config.settings.AppSettings
import rx.javafx.kt.actionEvents
import rx.javafx.kt.addTo
import tornadofx.*

/**
 * Created by developer on 2017/6/17.
 */
class RootLayoutView(val controller: ChatController): View() {
    public override val root = borderpane()

    init {
        with(root) {
            style {
                fontSize= AppSettings.settings.scaling.em
            }
            top = menubar {
                menu("File") {
                    item("Create Room").action { createRoomInteractive() }
                    item("Join Room") {
                        action { RoomFinder().open() }
                    }
                    item("Preferences").action {
                        find(PreferenceWindow::class).openModal()
                    }
                    item("Quit").action {
                        FX.primaryStage.close()
                    }
                }
                menu("Room") {
                    item("Invite Member"){
                        action { ask_invite_member() }
                    }
                    item("Ban Member") {
                        action { runAskBanRoomMember() }
                    }
                }
                menu("Me") {
                    item("Upload media").actionEvents().addTo(
                            guiEvents.updateAvatar)
                    item("Update my name").action { controller.updateMyAlias() }
                }
            }
        }
    }
}
