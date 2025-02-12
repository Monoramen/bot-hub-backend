import {Link} from "@heroui/react";

export default function InlineKeyboardLink() {
  return (
    <div className="flex gap-4">
      <Link color="secondary" href="#">
        Secondary
      </Link>
      <Link color="success" href="#">
        Success
      </Link>

    </div>
  );
}
